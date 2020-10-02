package nl.lukasmiedema.locationquest.controller.admin

import nl.lukasmiedema.locationquest.dao.GamesDao
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("admin/games/{game}/quests")
class QuestListController {

	@Autowired private lateinit var gamesDao: GamesDao
	@Autowired private lateinit var questDao: QuestDao
	@Autowired private lateinit var environment: Environment

	@GetMapping()
	fun get(@PathVariable("game") gameId: Int, model: Model): String {
		val game = gamesDao.getGame(gameId) ?: throw ResourceNotFoundException("No game with the id ${gameId}")
		model.addAttribute("game", game)

		val chapters = questDao.getChaptersByGame(game.gameId)
		val allQuests = questDao.getQuestsByGame(game.gameId)
		val requiredQuests = allQuests.filter { it.required }
		val optionalQuests = allQuests.filterNot { it.required }
		model.addAttribute("quests", requiredQuests + optionalQuests)
		model.addAttribute("chapters", chapters)
		model.addAttribute("domain", environment["locationquest.domain"])

		return "admin/QuestList";
	}
}
