package nl.lukasmiedema.locationquest.config

import nl.lukasmiedema.locationquest.controller.MenuInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Validator
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean


/**
 * @author Lukas Miedema
 */
@Configuration
open class WebMvcConfig : WebMvcConfigurerAdapter() {

	@Autowired private lateinit var menuInterceptor: MenuInterceptor
	@Autowired private lateinit var messageSource: MessageSource

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(menuInterceptor)
	}

	@Bean
	override fun getValidator(): Validator {
		val factory = LocalValidatorFactoryBean()
		factory.setValidationMessageSource(messageSource)
		return factory
	}
}