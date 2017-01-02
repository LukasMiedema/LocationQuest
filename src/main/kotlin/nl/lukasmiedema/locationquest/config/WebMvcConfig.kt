package nl.lukasmiedema.locationquest.config

import nl.lukasmiedema.locationquest.controller.MenuInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * @author Lukas Miedema
 */
@Configuration
open class WebMvcConfig : WebMvcConfigurerAdapter() {

	@Autowired private lateinit var menuInterceptor: MenuInterceptor

	override fun addInterceptors(registry: InterceptorRegistry?) {
		registry!!.addInterceptor(menuInterceptor)
	}
}