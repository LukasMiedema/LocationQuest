package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.controller.GamesController
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 */
@Controller
@RequestMapping(GamesController.URL + "/{game}/dashboard/history")
open class HistoryController {

	@Autowired private lateinit var sql: DSLContext

	@GetMapping
	open fun getHistory(
			@ModelAttribute("game") game: Game,
			model: Model,
			@AuthenticationPrincipal player: Player): String {
		model.addAttribute("activeTab", "HistoryTab")
		return "Dashboard"
	}
}