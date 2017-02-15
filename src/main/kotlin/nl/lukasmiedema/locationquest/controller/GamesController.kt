package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.GameInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
@RequestMapping(GamesController.URL)
open class GamesController {

	companion object {
		const val URL = "games"
	}

	@Autowired private lateinit var sql: DSLContext

	@GetMapping
	open fun getChooseGame(
			model: Model,
			@AuthenticationPrincipal player: Player): String {

		// Load all own games
		val g = Tables.GAME.`as`("g")
		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")

		val ownGames = sql
				.select(
						g.GAME_ID,
						g.NAME,
						g.TIMESTAMP,
						g.ACTIVE,
						g.ALLOW_NEW_MEMBERS,
						t.NAME.`as`("TEAM_NAME"),
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						tp.join(t) // find the team for every team player
								.on(t.TEAM_ID.eq(tp.TEAM_ID))
						.join(g) // find all games for every team player
								.on(t.GAME_ID.eq(g.GAME_ID))
				)
				.where(tp.PLAYER_ID.eq(player.playerId))
				.fetchInto(GameInfoDto::class.java)

		// Load all open games
		val openGames = sql
				.select(
						g.GAME_ID,
						g.NAME,
						g.TIMESTAMP,
						g.ACTIVE,
						g.ALLOW_NEW_MEMBERS,
						t.NAME.`as`("TEAM_NAME"),
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						g.leftJoin(t.join(tp)
									.on(tp.TEAM_ID.eq(t.TEAM_ID))
									.and(tp.PLAYER_ID.eq(player.playerId)))
								.on(t.GAME_ID.eq(g.GAME_ID))
				)
				.fetchInto(GameInfoDto::class.java)

		model.addAttribute("ownGames", ownGames)
		model.addAttribute("openGames", openGames)
		return "Games"
	}
}