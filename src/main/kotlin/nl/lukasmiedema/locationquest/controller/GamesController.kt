package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.GameInfoDto
import nl.lukasmiedema.locationquest.dto.PageDto
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.entity.tables.pojos.Team
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.session.PlayerSessionRepository
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.sql.Timestamp
import java.util.*
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
						g.ID,
						g.NAME,
						g.TIMESTAMP,
						g.ACTIVE,
						g.ALLOW_NEW_MEMBERS,
						t.NAME.`as`("TEAM_NAME"),
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						tp.join(t) // find the team for every team player
								.on(t.ID.eq(tp.TEAM_ID))
						.join(g) // find all games for every team player
								.on(t.GAME_ID.eq(g.ID))
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
						t.NAME.`as`("TEAM_NAME"),
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						g.leftJoin(t.join(tp)
									.on(tp.TEAM_ID.eq(t.ID))
									.and(tp.PLAYER_SESSION_ID.eq(player.sessionId)))
								.on(t.GAME_ID.eq(g.ID))
				)
				.fetchInto(GameInfoDto::class.java)

		model.addAttribute("ownGames", ownGames)
		model.addAttribute("openGames", openGames)
		return "Games"
	}
}