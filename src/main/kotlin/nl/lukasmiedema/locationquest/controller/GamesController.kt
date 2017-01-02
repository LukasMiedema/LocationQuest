package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.GameInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.session.PlayerSessionRepository
import org.jooq.DSLContext
import org.jooq.impl.DSL.count
import org.jooq.impl.DSL.select
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpSession

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
		const val GAMES_VIEW = "Games"
	}

	@Autowired private lateinit var sql: DSLContext

	@GetMapping
	open fun getChooseGame(model: Model, @AuthenticationPrincipal player: Player): String {

		// Load all own games
		val g = Tables.GAME.`as`("g")
		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")

		val ownGames = sql
				.select(
						g.ID,
						g.NAME,
						g.TIMESTAMP,
						g.ACTIVE,
						g.ALLOW_NEW_MEMBERS,
						select(count())
								.from(Tables.TEAM_PLAYER)
								.where(Tables.TEAM_PLAYER.GAME_ID.eq(g.ID))
								.asField<Any>("MEMBER_COUNT"),
						tp.TEAM_NAME,
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						tp.join(t) // find the team for every team player
								.on(t.GAME_ID.eq(tp.GAME_ID))
								.and(t.NAME.eq(tp.TEAM_NAME))
						.join(g) // find all games for every team player
								.on(tp.GAME_ID.eq(g.ID))
				)
				.where(tp.PLAYER_SESSION_ID.eq(player.sessionId))
				.fetchInto(GameInfoDto::class.java)

		// Load all open games
		val openGames = sql
				.select(
						g.ID,
						g.NAME,
						g.TIMESTAMP,
						g.ACTIVE,
						g.ALLOW_NEW_MEMBERS,
						select(count())
								.from(Tables.TEAM_PLAYER)
								.where(Tables.TEAM_PLAYER.GAME_ID.eq(g.ID))
								.asField<Any>("MEMBER_COUNT"),
						tp.TEAM_NAME,
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						g.leftJoin(tp)
								.on(tp.GAME_ID.eq(g.ID))
								.and(tp.PLAYER_SESSION_ID.eq(player.sessionId))
						.leftJoin(t)
								.on(t.NAME.eq(tp.TEAM_NAME))
								.and(t.GAME_ID.eq(tp.GAME_ID))
				)
				.fetchInto(GameInfoDto::class.java)

		model.addAttribute("ownGames", ownGames)
		model.addAttribute("openGames", openGames)

		model.addAttribute("title", "Kies een spel")
		return GAMES_VIEW
	}
}