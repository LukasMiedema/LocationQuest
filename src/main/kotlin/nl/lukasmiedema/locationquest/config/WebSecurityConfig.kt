package nl.lukasmiedema.locationquest.config

import nl.lukasmiedema.locationquest.LocationQuestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.context.SecurityContextPersistenceFilter
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Lukas Miedema
 */
@Configuration
open class WebSecurityConfig : WebSecurityConfigurerAdapter(false) {

	override fun configure(http: HttpSecurity) {
		http
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				.antMatchers(LocationQuestApplication.REST_PREFIX + "players/**").permitAll() // players endpoint is allowed
				.antMatchers(LocationQuestApplication.REST_PREFIX + "**").authenticated() // rest of the endpoints are not
				.anyRequest().permitAll(); // but static resources are
	}
}