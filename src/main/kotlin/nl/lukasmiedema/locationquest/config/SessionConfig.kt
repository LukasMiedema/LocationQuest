package nl.lukasmiedema.locationquest.config

import nl.lukasmiedema.locationquest.LocationQuestApplication
import nl.lukasmiedema.locationquest.session.PlayerSessionRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.MapSessionRepository
import org.springframework.session.SessionRepository
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.stereotype.Component

/**
 * @author Lukas Miedema
 */
@Configuration
@EnableSpringHttpSession
 open class SessionConfig {

	@Bean
	open fun cookieSerializer() : CookieSerializer {
		val serializer = DefaultCookieSerializer()
		serializer.setCookieName(LocationQuestApplication.COOKIE_NAME);
		serializer.setCookiePath("/")
		serializer.setCookieMaxAge(Integer.MAX_VALUE);
		return serializer
	}
}