package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.controller.GamesController
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.ChapterDto
import nl.lukasmiedema.locationquest.dto.MessageDto
import nl.lukasmiedema.locationquest.dto.QuestPhase
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.service.I18nService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author Lukas Miedema
 */
@Controller
@RequestMapping("games/{game}/dashboard")
class QuestController {

	@Autowired private lateinit var i18n: I18nService
	@Autowired private lateinit var questDao: QuestDao

	@GetMapping
	fun getQuest(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			@ModelAttribute("messages") messages: MutableList<MessageDto>,
			@RequestParam("claim", defaultValue = "false") claimed: Boolean,
			model: Model): String {

		val questPhase: QuestPhase
		val quest: Quest?
		val chapter: ChapterDto?

		if (!game.active) {

			// Game isn't active --> no quest
			questPhase = QuestPhase.CLOSED
			quest = null
			chapter = null

		} else {

			// Get next question
			quest = questDao.getNextQuest(game.gameId!!, team.teamId!!)
			questPhase = if (quest == null) QuestPhase.DONE else QuestPhase.RUNNING
			chapter = if (quest == null) null else questDao.getChapter(quest.chapterId)?.let(::ChapterDto)
		}

		// Add a message if this was a redirect
		if (claimed) {
			messages.add(MessageDto(MessageDto.MessageType.SUCCESS, i18n["dashboard.scan.accepted"]))
		}

		model.addAttribute("questPhase", questPhase)
		model.addAttribute("quest", quest)
		model.addAttribute("chapter", chapter)
		model.addAttribute("activeTab", "QuestTab")
		return "Dashboard"
	}
}
