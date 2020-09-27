package nl.lukasmiedema.locationquest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect

/**
 * Adds some dialect beans to Thymeleaf.
 * @author Lukas Miedema
 */
@Configuration
class ThymeleafConfig {

	@Bean
	fun springSecurityDialect(): SpringSecurityDialect {
		return SpringSecurityDialect()
	}

}
