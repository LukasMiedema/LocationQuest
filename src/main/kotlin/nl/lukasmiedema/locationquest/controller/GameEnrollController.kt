package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.GamesDao
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
@RequestMapping("games")
open class GameEnrollController {

	@Autowired private lateinit var gamesDao: GamesDao

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
		val alreadyEnrolled = gamesDao.isEnrolled(gameId, player.playerId)
		if (alreadyEnrolled) {
			return "redirect:/games/$gameId/dashboard"
		}

		// Get the game and teams
		val game: Game = gamesDao.getGame(gameId) ?: throw ResourceNotFoundException("No such game: $gameId")
		val teams = gamesDao.getTeamsDetailed(gameId)

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

		// TODO: check if game is open!

		// Save and continue
		gamesDao.enroll(teamSelectionDto.teamId!!, player.playerId)
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

		// TODO: check if game is open!

		// Save team
		val newTeam = gamesDao.createTeam(gameId, teamCreationDto.colorInt!!, teamCreationDto.name!!)
		gamesDao.enroll(newTeam.teamId, player.playerId)

		return "redirect:/games/$gameId/dashboard"
	}
}