package nl.lukasmiedema.locationquest.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*


/**
 * @author Lukas Miedema
 */
@Configuration
class I18nConfig {

	@Bean
	fun localeResolver(): LocaleResolver {
		val resolver = CookieLocaleResolver()
		resolver.cookieName = "Locale"
		resolver.setDefaultLocale(Locale("nl"))
		return resolver
	}
}
