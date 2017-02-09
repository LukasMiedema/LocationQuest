package nl.lukasmiedema.locationquest.controller

import org.springframework.boot.autoconfigure.web.ErrorViewResolver
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

/**
 * @author Lukas Miedema
 */
@Component
open class ErrorController : ErrorViewResolver {

	override fun resolveErrorView(request: HttpServletRequest?, status: HttpStatus?, model: MutableMap<String, Any>?): ModelAndView {
		val mv = ModelAndView("Error")
		mv.modelMap.addAllAttributes(model)
		mv.modelMap.addAttribute("title", "Oh snap")
		return mv
	}
}