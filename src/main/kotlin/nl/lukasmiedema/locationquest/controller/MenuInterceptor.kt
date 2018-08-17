package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.GamesDao
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
class MenuInterceptor : HandlerInterceptorAdapter() {

	companion object {
		private const val DEFAULT_LAYOUT = "components/Page"
		private const val DEFAULT_VIEW_ATTRIBUTE_NAME = "view"
	}

	@Autowired private lateinit var gamesDao: GamesDao

	@ModelAttribute("page")
	fun getPage(@AuthenticationPrincipal player: Player?): PageDto {
		if (player == null) {
			return PageDto(false, null, null)
		} else {
			val games = gamesDao.getEnrolledGames(player.playerId)
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

		val originalViewName = modelAndView.viewName!!
		if (originalViewName.startsWith("redirect:"))
			return

		modelAndView.viewName = DEFAULT_LAYOUT
		modelAndView.addObject(DEFAULT_VIEW_ATTRIBUTE_NAME, originalViewName)
	}
}
