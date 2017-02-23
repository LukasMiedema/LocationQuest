package nl.lukasmiedema.locationquest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*


/**
 * @author Lukas Miedema
 */
@Configuration
open class I18nConfig {

	@Bean
	open fun localeResolver(): LocaleResolver {
		val slr = SessionLocaleResolver()
		slr.setDefaultLocale(Locale("de"))
		return slr
	}
}