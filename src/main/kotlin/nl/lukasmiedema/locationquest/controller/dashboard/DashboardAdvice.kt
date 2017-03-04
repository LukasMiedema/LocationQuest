package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.dao.GamesDao
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.GameTabDto
import nl.lukasmiedema.locationquest.dto.MessageDto
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.exception.UnauthorizedException
import nl.lukasmiedema.locationquest.service.MarkdownService
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

/**
 * Controller Advice for all dashboard controllers.
 * @author Lukas Miedema
 */
@PreAuthorize("isAuthenticated()")
@ControllerAdvice(basePackageClasses = arrayOf(DashboardAdvice::class))
open class DashboardAdvice {

	companion object {
		private val TABS: Array<GameTabDto> = arrayOf(
				GameTabDto("QuestTab", ""),
				GameTabDto("HistoryTab", "history"),
				GameTabDto("TeamTab", "team")
		)
	}

	@Autowired private lateinit var gamesDao: GamesDao
	@Autowired private lateinit var questDao: QuestDao
	@Autowired private lateinit var markdown: MarkdownService

	@ModelAttribute("markdown")
	open fun markdown() = markdown // make the markdown service available for all templates to use

	@ModelAttribute("tabs")
	open fun tabs() = TABS

	@ModelAttribute("game")
	open fun getGame(@PathVariable("game") gameId: Int): Game {
		return gamesDao.getGame(gameId) ?: throw ResourceNotFoundException("No such game")
	}

	@ModelAttribute("team")
	open fun getTeamInfo(
			@PathVariable("game") gameId: Int,
			@AuthenticationPrincipal player: Player): TeamInfoDto {

		return gamesDao.getTeamDetailed(gameId, player.playerId) ?: throw UnauthorizedException("Not enrolled")
	}

	@ModelAttribute("chapters")
	open fun getChapters(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto) = questDao.getQuestChapterByGameAndTeam(game.gameId, team.teamId!!)

	@ModelAttribute("inventory")
	open fun getInventory(@ModelAttribute("team") team: TeamInfoDto) = questDao.getInventory(team.teamId!!)

	@ModelAttribute("messages")
	open fun getMessages() = ArrayList<MessageDto>()
}