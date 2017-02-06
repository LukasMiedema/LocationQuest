package nl.lukasmiedema.locationquest.config

import nl.lukasmiedema.locationquest.LocationQuestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.context.SecurityContextPersistenceFilter
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Lukas Miedema
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig : WebSecurityConfigurerAdapter(false) {

	override fun configure(http: HttpSecurity) {
		// all resources are ok (method security is used on the controllers)
		http
				.csrf().disable()
				.headers().frameOptions().sameOrigin().and()
				.authorizeRequests().anyRequest().permitAll()
	}
}