package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.GameTabDto
import nl.lukasmiedema.locationquest.dto.MessageDto
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.exception.UnauthorizedException
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

	@Autowired private lateinit var sql: DSLContext
	@Autowired private lateinit var questDao: QuestDao

	@ModelAttribute("tabs")
	open fun tabs() = TABS

	@ModelAttribute("game")
	open fun getGame(@PathVariable("game") gameId: Int): Game {
		val game = sql
				.selectFrom(Tables.GAME)
				.where(Tables.GAME.GAME_ID.eq(gameId))
				.fetchOneInto(Game::class.java)
		if (game == null) {
			throw ResourceNotFoundException("No such game")
		} else {
			return game
		}
	}

	@ModelAttribute("team")
	open fun getTeamInfo(
			@PathVariable("game") gameId: Int,
			@AuthenticationPrincipal player: Player): TeamInfoDto {

		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")

		val teamInfo = sql
				.select(
						t.TEAM_ID,
						t.NAME,
						t.COLOR,
						DSL.select(DSL.count())
								.from(Tables.TEAM_PLAYER)
								.where(Tables.TEAM_PLAYER.TEAM_ID.eq(t.TEAM_ID))
								.asField<Any>("MEMBER_COUNT")
				)
				.from(t.join(tp).on(t.TEAM_ID.eq(tp.TEAM_ID)))
				.where(t.GAME_ID.eq(gameId))
				.and(tp.PLAYER_ID.eq(player.playerId))
				.fetchOneInto(TeamInfoDto::class.java)

		if (teamInfo == null) {
			throw UnauthorizedException("Not enrolled")
		} else {
			return teamInfo
		}
	}

	@ModelAttribute("chapters")
	open fun getChapters(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto) = questDao.getChaptersByGame(game.gameId, team.teamId!!)

	@ModelAttribute("inventory")
	open fun getInventory(@ModelAttribute("team") team: TeamInfoDto) = questDao.getInventory(team.teamId!!)

	@ModelAttribute("messages")
	open fun getMessages() = ArrayList<MessageDto>()
}