package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.GamesDao
import nl.lukasmiedema.locationquest.dto.GameInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Controls all the game related pages
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("games")
class GamesController {

	@Autowired private lateinit var gamesDao: GamesDao

	@GetMapping
	fun getChooseGame(
			model: Model,
			@AuthenticationPrincipal player: Player): String {

		// Load all games
		val ownGames = gamesDao.getEnrolledGamesDetailed(player.playerId)
		val openGames = gamesDao.getOpenGamesDetailed(player.playerId)

		model.addAttribute("ownGames", ownGames)
		model.addAttribute("openGames", openGames)

		return "Games"
	}
}
