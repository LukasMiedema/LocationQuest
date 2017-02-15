package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.controller.GamesController
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.MessageDto
import nl.lukasmiedema.locationquest.dto.QuestType
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 */
@Controller
@RequestMapping(GamesController.URL + "/{game}/dashboard")
open class QuestController {

	@Autowired private lateinit var questDao: QuestDao

	@GetMapping
	open fun getQuest(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			@ModelAttribute("messages") messages: MutableList<MessageDto>,
			model: Model): String {

		val questType: QuestType
		val quest: Quest?

		if (!game.active) {

			// Game isn't active --> no quest
			questType = QuestType.CLOSED
			quest = null

		} else {

			// Get next question
			quest = questDao.getNextQuest(game.gameId!!, team.teamId!!)
			questType = if (quest == null) QuestType.DONE else QuestType.valueOf(quest.type)
		}

		model.addAttribute("questType", questType)
		model.addAttribute("quest", quest)

		model.addAttribute("activeTab", "QuestTab")
		return "Dashboard"
	}
}