package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.controller.GamesController
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.*
import nl.lukasmiedema.locationquest.entity.tables.pojos.ClaimedQuest
import nl.lukasmiedema.locationquest.entity.tables.pojos.Collectible
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.service.I18nService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.util.*

/**
 * @author Lukas Miedema
 */
@Controller
@RequestMapping("games/{game}/scan/{code}")
class ScanController {

	@Autowired private lateinit var questDao: QuestDao
	@Autowired private lateinit var i18n: I18nService

	@ModelAttribute("quest", binding = false)
	fun getQuest(@ModelAttribute game: Game, @PathVariable("code") code: UUID): QuestDto =
			questDao.getQuestByQR(game.gameId, code) ?: throw ResourceNotFoundException("No such quest")

	@ModelAttribute("items", binding = false)
	fun getQuestItems(@ModelAttribute("quest") quest: Quest) =
			questDao.getQuestItems(quest.questId)

	@ModelAttribute("yieldsInventory", binding = false)
	fun getYieldsInventory(@ModelAttribute("items") items: QuestInventoryDto) = items.yieldsInventory

	@ModelAttribute("requiresInventory", binding = false)
	fun getRequiresInventory(@ModelAttribute("items") items: QuestInventoryDto) = items.requiresInventory

	@ModelAttribute("passcode")
	fun getPasscode() = "" // default is empty. This can be overridden by the controller

	@ModelAttribute("chapter")
	fun getChapter(@ModelAttribute("quest") quest: Quest) = questDao.getChapter(quest.chapterId)

	/**
	 * Returns details about the posibility of a claim.
	 */
	@ModelAttribute("scanCode", binding = false)
	fun getClaimDetails(
			@ModelAttribute("quest") quest: Quest,
			@ModelAttribute("requiresInventory") requiresInventory: InventoryDto,
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			@ModelAttribute("inventory") inventory: InventoryDto): ScanCode {

		// Duplication check
		if (questDao.isClaimed(quest.questId, team.teamId!!)) {
			return ScanCode.DUPLICATE
		}

		// Eligibility check
		val nextQuest: Quest? = questDao.getNextQuest(game.gameId, team.teamId!!)
		if (quest.required && quest.questId != nextQuest?.questId) {
			return ScanCode.INELIGIBLE
		}

		// Check dependencies
		if (!inventory.has(requiresInventory)) {
			return ScanCode.UNSATISFIED_DEPENDENCY
		}

		// All ok!
		return ScanCode.CLAIMABLE
	}

	@GetMapping
	fun scanQuest(
			@ModelAttribute("messages") messages: MutableList<MessageDto>,
			@ModelAttribute("inventory") inventory: InventoryDto,
			@ModelAttribute("scanCode") scanCode: ScanCode,
			model: Model): String {

		// Set error messages
		when (scanCode) {
			ScanCode.CLAIMABLE -> Unit // all ok

			ScanCode.UNSATISFIED_DEPENDENCY -> messages.add(MessageDto(MessageDto.MessageType.WARNING,
					i18n["dashboard.scan.unsatisfiedDependency"]))

			ScanCode.DUPLICATE -> messages.add(MessageDto(MessageDto.MessageType.WARNING,
					i18n["dashboard.scan.duplicate"]))

			ScanCode.INELIGIBLE -> messages.add(MessageDto(MessageDto.MessageType.WARNING,
					i18n["dashboard.scan.ineligible"]))
		}

		model.addAttribute("claimable", scanCode == ScanCode.CLAIMABLE)
		model.addAttribute("activeTab", "ScanTab")
		return "Dashboard"
	}

	@PostMapping
	fun claimQuest(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			@ModelAttribute("quest") quest: QuestDto,
			@RequestParam("passcode", required = false) passcode: String?,
			@RequestParam("questAnswer", required = false) questAnswer: Int?,

			@ModelAttribute("messages") messages: MutableList<MessageDto>,
			@ModelAttribute("inventory") inventory: InventoryDto,
			@ModelAttribute("scanCode") scanCode: ScanCode,
			model: Model): String {

		// Check if the quest is claimable
		if (scanCode == ScanCode.CLAIMABLE) {

			if (quest.passcodeText != null && quest.passcodeText != passcode) {
				// Wrong code
				messages.add(MessageDto(MessageDto.MessageType.WARNING, i18n["dashboard.scan.wrongCode"]))
				model.addAttribute("passcode", passcode)

			} else if (quest.answers.isNotEmpty() && (questAnswer == null || questAnswer !in quest.answers.indices )) {
				// Wrong answer or answer out of bounds
				// This can only be caused by form hacking
				messages.add(MessageDto(MessageDto.MessageType.WARNING, i18n["dashboard.scan.wrongCode"]))
			} else {
				// Store
				val claim = ClaimedQuest(quest.questId, team.teamId, questAnswer?.let { quest.answers[it].label },
						Timestamp(System.currentTimeMillis()))
				questDao.insertClaim(claim)

				// Redirect to the normal page
				return "redirect:/games/${game.gameId}/dashboard?claim=true"
			}
		}

		// Show the view with errors
		return scanQuest(messages, inventory, scanCode, model)
	}
}
