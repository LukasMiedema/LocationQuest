package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.PageDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.session.PlayerSessionRepository
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.springframework.web.servlet.ModelAndView
import sun.audio.AudioPlayer.player
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * @author Lukas Miedema
 */
@ControllerAdvice
@Transactional
open class MenuInterceptor : HandlerInterceptorAdapter() {

	companion object {
		private val DEFAULT_LAYOUT = "Page"
		private val DEFAULT_VIEW_ATTRIBUTE_NAME = "view"
	}

	@Autowired private lateinit var sql: DSLContext

	@ModelAttribute("page")
	open fun getPage(@AuthenticationPrincipal player: Player?): PageDto {
		if (player == null) {
			return PageDto(false, null, null)
		} else {
			val games = sql
					.select(*Tables.GAME.fields())
					.from(Tables.GAME
							.join(Tables.TEAM).on(Tables.TEAM.GAME_ID.eq(Tables.GAME.ID))
							.join(Tables.TEAM_PLAYER).on(Tables.TEAM_PLAYER.TEAM_ID.eq(Tables.TEAM.ID))
					)
					.where(Tables.TEAM_PLAYER.PLAYER_SESSION_ID.eq(player.sessionId))
					.fetchInto(Game::class.java)
			return PageDto(true, player.name, games)
		}
	}

	override fun postHandle(
			request: HttpServletRequest,
			response: HttpServletResponse,
			handler: Any,
			modelAndView: ModelAndView?) {

		if (modelAndView == null || !modelAndView.hasView())
			return

		val originalViewName = modelAndView.viewName
		if (originalViewName.startsWith("redirect:"))
			return

		modelAndView.viewName = DEFAULT_LAYOUT
		modelAndView.addObject(DEFAULT_VIEW_ATTRIBUTE_NAME, originalViewName)
	}
}