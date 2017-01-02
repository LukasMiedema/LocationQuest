package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.MenuBarDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.session.PlayerSessionRepository
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.springframework.web.servlet.ModelAndView
import sun.audio.AudioPlayer.player
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * @author Lukas Miedema
 */
@Component
@Transactional
open class MenuInterceptor : HandlerInterceptorAdapter() {

	companion object {
		private val DEFAULT_LAYOUT = "Page"
		private val DEFAULT_VIEW_ATTRIBUTE_NAME = "view"
		private val MENU_BAR = "menu"
	}

	@Autowired private lateinit var sql: DSLContext

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

		// Get data for the nav bar
		val auth: Authentication? = SecurityContextHolder.getContext().authentication
		if (auth is PlayerSessionRepository.SessionAuthentication) {
			val player = auth.player
			val games = sql
					.select(*Tables.GAME.fields())
					.from(Tables.GAME
							.join(Tables.TEAM_PLAYER)
							.on(Tables.TEAM_PLAYER.GAME_ID.eq(Tables.GAME.ID))
					)
					.where(Tables.TEAM_PLAYER.PLAYER_SESSION_ID.eq(player.sessionId))
					.fetchInto(Game::class.java)
			modelAndView.addObject(MENU_BAR, MenuBarDto(true, player.name, games))
		} else {
			modelAndView.addObject(MENU_BAR, MenuBarDto(false, null, null))
		}
	}
}