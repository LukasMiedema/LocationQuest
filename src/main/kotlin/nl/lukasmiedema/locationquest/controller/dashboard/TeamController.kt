package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.controller.GamesController
import nl.lukasmiedema.locationquest.dao.GamesDao
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
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
@RequestMapping("games/{game}/dashboard/team")
class TeamController {

	@Autowired private lateinit var gamesDao: GamesDao

	@GetMapping
	fun getTeam(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			model: Model,
			@AuthenticationPrincipal player: Player): String {

		val teamMembers = gamesDao.getTeamMembers(team.teamId!!)

		model.addAttribute("teamMembers", teamMembers)
		model.addAttribute("activeTab", "TeamTab")
		model.addAttribute("player", player)
		return "Dashboard"
	}
}
