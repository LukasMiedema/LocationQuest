package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.controller.GamesController
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.*
import nl.lukasmiedema.locationquest.entity.tables.pojos.ClaimedQuest
import nl.lukasmiedema.locationquest.entity.tables.pojos.Collectible
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
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
@RequestMapping(GamesController.URL + "/{game}/scan/{code}")
open class ScanController {

	@Autowired private lateinit var questDao: QuestDao

	@ModelAttribute("quest")
	open fun getQuest(@PathVariable("code") code: UUID) =
			questDao.getQuestByQR(code) ?: throw ResourceNotFoundException("No such quest")

	@ModelAttribute("required")
	open fun getRequirements(@ModelAttribute("quest") quest: Quest) =
			questDao.getQuestCollectibles(quest.questId)

	/**
	 * Returns details about the posibility of a claim.
	 */
	@ModelAttribute("scanCode")
	open fun getClaimDetails(
			@ModelAttribute("quest") quest: Quest,
			@ModelAttribute("required") required: List<QuestCollectibleDto>,
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
		if (!inventory.has(required)) {
			return ScanCode.UNSATISFIED_DEPENDENCY
		}

		// All ok!
		return ScanCode.CLAIMABLE
	}

	@GetMapping
	open fun scanQuest(
			@ModelAttribute("messages") messages: MutableList<MessageDto>,
			@ModelAttribute("inventory") inventory: InventoryDto,
			@ModelAttribute("required") required: List<QuestCollectibleDto>,
			@ModelAttribute("scanCode") scanCode: ScanCode,
			model: Model): String {

		// Set error messages
		when (scanCode) {
			ScanCode.CLAIMABLE -> Unit // all ok

			ScanCode.UNSATISFIED_DEPENDENCY -> messages.add(MessageDto(MessageDto.MessageType.WARNING,
					"Deze quest heeft items nodig die je nog niet hebt"))

			ScanCode.DUPLICATE -> messages.add(MessageDto(MessageDto.MessageType.WARNING,
					"Deze quest is al geclaimed door je team"))

			ScanCode.INELIGIBLE -> messages.add(MessageDto(MessageDto.MessageType.WARNING,
					"Deze quest kan nog niet geclaimed worden. Doe je ze wel in de goede volgorde?"))
		}

		// Create inventory dtos for yields and requires
		val yieldsInventory = InventoryDto(
				required.map({ InventoryDto.InventoryItem(it.collectible, it.questCollectible.yields) })
						.filter { it.count != 0 })

		val requiresInventory = InventoryDto(
				required.map({ InventoryDto.InventoryItem(it.collectible, it.questCollectible.requires) })
						.filter { it.count != 0 })

		model.addAttribute("yieldsInventory", yieldsInventory)
		model.addAttribute("requiresInventory", requiresInventory)

		model.addAttribute("claimable", scanCode == ScanCode.CLAIMABLE)
		model.addAttribute("activeTab", "ScanTab")
		return "Dashboard"
	}

	@PostMapping
	open fun claimQuest(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			@ModelAttribute("quest") quest: Quest,
			@ModelAttribute("scanCode") scanCode: ScanCode): String {

		if (scanCode == ScanCode.CLAIMABLE) {

			// Store
			val claim = ClaimedQuest(quest.questId, team.teamId!!, Timestamp(System.currentTimeMillis()))
			questDao.insertClaim(claim)

		}

		// Redirect to the normal page
		return "redirect:/${GamesController.URL}/${game.gameId}/dashboard"
	}
}