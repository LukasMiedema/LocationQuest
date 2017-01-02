package nl.lukasmiedema.locationquest.controller

import org.springframework.boot.autoconfigure.web.ErrorViewResolver
import org.springframework.boot.web.servlet.ErrorPage
import org.springframework.boot.web.servlet.ErrorPageRegistrar
import org.springframework.boot.web.servlet.ErrorPageRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author Lukas Miedema
 */
@Component
open class CustomErrorViewResolver : ErrorViewResolver {

	override fun resolveErrorView(request: HttpServletRequest?, status: HttpStatus?, model: MutableMap<String, Any>?): ModelAndView {
		val mv = ModelAndView("Error")
		mv.modelMap.addAllAttributes(model)
		mv.modelMap.addAttribute("title", "Oh snap")
		return mv
	}
}