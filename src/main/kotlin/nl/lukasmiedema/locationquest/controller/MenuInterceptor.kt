package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.GamesDao
import nl.lukasmiedema.locationquest.dto.MessageDto
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
import java.util.ArrayList
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

	@ModelAttribute("page", binding = false)
	fun getPage(@AuthenticationPrincipal player: Player?): PageDto {
		return if (player == null) {
			PageDto(false, null, null)
		} else {
			val games = gamesDao.getEnrolledGames(player.playerId)
			PageDto(true, player.name, games)
		}
	}

	@ModelAttribute("messages", binding = false)
	fun getMessages() = ArrayList<MessageDto>()

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
