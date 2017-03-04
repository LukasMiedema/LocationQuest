package nl.lukasmiedema.locationquest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect

/**
 * Adds some dialect beans to Thymeleaf.
 * @author Lukas Miedema
 */
@Configuration
open class ThymeleafConfig {

	@Bean
	open fun springSecurityDialect(): SpringSecurityDialect {
		return SpringSecurityDialect()
	}

}