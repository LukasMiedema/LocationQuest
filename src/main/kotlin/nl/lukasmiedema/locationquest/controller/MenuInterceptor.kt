package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.PageDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.Tables.*
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * @author Lukas Miedema
 */
@ControllerAdvice
@Transactional
open class MenuInterceptor : HandlerInterceptorAdapter() {

	companion object {
		private val DEFAULT_LAYOUT = "components/Page"
		private val DEFAULT_VIEW_ATTRIBUTE_NAME = "view"
	}

	@Autowired private lateinit var sql: DSLContext

	@ModelAttribute("page")
	open fun getPage(@AuthenticationPrincipal player: Player?): PageDto {
		if (player == null) {
			return PageDto(false, null, null)
		} else {
			val games = sql
					.select(*GAME.fields())
					.from(GAME
							.join(TEAM).on(TEAM.GAME_ID.eq(GAME.GAME_ID))
							.join(TEAM_PLAYER).on(TEAM_PLAYER.TEAM_ID.eq(TEAM.TEAM_ID))
					)
					.where(TEAM_PLAYER.PLAYER_ID.eq(player.playerId))
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