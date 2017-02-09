package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.TeamCreationDto
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.dto.TeamSelectionDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.awt.Color
import javax.validation.Valid

/**
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(GamesController.URL)
open class GameEnrollController {

	@Autowired private lateinit var sql: DSLContext

	@ModelAttribute
	open fun teamSelectionDto() = TeamSelectionDto()

	@ModelAttribute
	open fun teamCreationDto(): TeamCreationDto {
		val dto = TeamCreationDto()
		dto.color = String.format("#%06X", 0xFFFFFF and Color.getHSBColor(Math.random().toFloat(), 1.0f, 1.0f).rgb)
		return dto
	}

	@GetMapping("/{game}")
	open fun getChooseGameTeam(
			model: Model,
			@PathVariable("game") gameId: Int,
			@AuthenticationPrincipal player: Player): String {

		// Check if the user is already in a team for this game
		val alreadyEnrolled = sql.fetchExists(
				DSL.selectFrom(
						Tables.TEAM.join(Tables.TEAM_PLAYER)
								.on(Tables.TEAM.ID.eq(Tables.TEAM_PLAYER.TEAM_ID))
				)
				.where(Tables.TEAM_PLAYER.PLAYER_ID.eq(player.playerId))
				.and(Tables.TEAM.GAME_ID.eq(gameId))
		)
		if (alreadyEnrolled) {
			return "redirect:/games/$gameId/dashboard"
		}

		// Get the game
		val game: Game = sql
				.selectFrom(Tables.GAME)
				.where(Tables.GAME.ID.eq(gameId))
				.fetchOneInto(Game::class.java)
				?: throw ResourceNotFoundException("No such game: $gameId")


		// Load all team info
		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")

		val teams = sql
				.select(
						t.ID,
						t.NAME,
						t.COLOR,
						DSL.select(DSL.count())
								.from(tp)
								.where(tp.TEAM_ID.eq(t.ID))
								.asField<Any>("MEMBER_COUNT")
				)
				.from(t).where(t.GAME_ID.eq(gameId))
				.fetchInto(TeamInfoDto::class.java);

		model.addAttribute("teams", teams)
		model.addAttribute("game", game)

		return "GameEnroll"
	}

	@PostMapping("/{game}/enroll")
	open fun postEnroll(
			model: Model,
			@PathVariable("game") gameId: Int,
			@AuthenticationPrincipal player: Player,
			@Valid teamSelectionDto: TeamSelectionDto,
			bindingResult: BindingResult): String {

		// Check for errors
		if (bindingResult.hasErrors()) {
			return getChooseGameTeam(model, gameId, player)
		}

		// Save and continue
		val tp = Tables.TEAM_PLAYER
		sql.insertInto(tp, tp.PLAYER_ID, tp.TEAM_ID).values(player.playerId, teamSelectionDto.teamId).execute()
		return "redirect:/games/$gameId/dashboard"
	}

	@PostMapping("/{game}/create")
	open fun postCreate(
			model: Model,
			@PathVariable("game") gameId: Int,
			@AuthenticationPrincipal player: Player,
			@Valid teamCreationDto: TeamCreationDto,
			bindingResult: BindingResult): String {

		// Check for errors
		if (bindingResult.hasErrors()) {
			return getChooseGameTeam(model, gameId, player)
		}

		// Save team
		val teamRecord = sql
				.insertInto(Tables.TEAM)
				.set(Tables.TEAM.GAME_ID, gameId)
				.set(Tables.TEAM.COLOR, teamCreationDto.colorInt)
				.set(Tables.TEAM.NAME, teamCreationDto.name)
				.returning(Tables.TEAM.ID)
				.fetchOne()

		// Save user
		sql.insertInto(Tables.TEAM_PLAYER).values(player.playerId, teamRecord.id).execute()
		return "redirect:/games/$gameId/dashboard"
	}
}