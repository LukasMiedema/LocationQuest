package nl.lukasmiedema.locationquest.controller.admin

import nl.lukasmiedema.locationquest.dao.GamesDao
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.QuestEditDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.service.MarkdownService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.security.SecureRandom
import javax.validation.Valid

/**
 * @author Lukas Miedema
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("admin/games/{gameId}/quests")
class QuestEditController {

	private val random = SecureRandom()
	private val secretChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
	private val secretLength = 5;

	@Autowired private lateinit var gamesDao: GamesDao
	@Autowired private lateinit var questDao: QuestDao
	@Autowired private lateinit var environment: Environment
	@Autowired private lateinit var markdown: MarkdownService

	@ModelAttribute("markdown", binding = false)
	fun markdown() = markdown

	@GetMapping("/{questId}", "/new")
	fun get(@PathVariable("gameId") gameId: Int, @PathVariable("questId", required = false) questId: Int?, model: Model): String {
		return view(gameId, questId, null, model)
	}

	@PostMapping("/{questId}", "/new")
	fun postEdit(@PathVariable("gameId") gameId: Int, @PathVariable("questId", required = false) questId: Int?,
				 @Valid @ModelAttribute dto: QuestEditDto, binding: BindingResult, model: Model): String {
		if (binding.hasErrors())
			return view(gameId, questId, dto, model)
		val quest = if (questId == null) Quest() else
			(questDao.getQuest(questId) ?: throw ResourceNotFoundException("No quest with questId = $questId"))
		quest.chapterId = dto.chapterId
		quest.name = dto.name
		quest.fetchText = dto.fetchText
		quest.scanText = dto.scanText
		quest.required = dto.required
		quest.qrCode = dto.qrCode
		quest.passcodeText = dto.passcodeText
		val updatedQuest = if (questId == null) questDao.insertQuest(quest) else questDao.updateQuest(quest)
		return "redirect:/admin/games/$gameId/quests/${updatedQuest.questId}"
	}

	fun view(gameId: Int, questId: Int?, dto: QuestEditDto?, model: Model): String {
		val game = gamesDao.getGame(gameId) ?: throw ResourceNotFoundException("No game with gameId = $gameId")
		model.addAttribute("game", game)
		if (questId == null) {
			// create
			model.addAttribute("questDto", dto ?: QuestEditDto().apply {
				qrCode = generateQrCode()
				fetchText = ""
				scanText = ""
			})
		} else {
			// edit
			val quest = questDao.getQuest(questId) ?: throw ResourceNotFoundException("No quest with questId = $questId")
			model.addAttribute("quest", quest)
			model.addAttribute("claimUrl", "${environment["locationquest.domain"]}/s/${gameId}/${quest.qrCode}")
			model.addAttribute("questDto", dto ?: QuestEditDto().apply {
				chapterId = quest.chapterId
				name = quest.name ?: ""
				fetchText = quest.fetchText ?: ""
				scanText = quest.scanText ?: ""
				passcodeText = quest.passcodeText ?: ""
				required = quest.required
				qrCode = quest.qrCode ?: generateQrCode()
			})
		}
		return "admin/QuestAdmin"
	}

	/**
	 * Generate a random code, which can be used as a QR code.
	 */
	fun generateQrCode(): String = (0 until secretLength)
			.map { secretChars[random.nextInt(secretChars.size)] }
			.joinToString(separator = "")
}
