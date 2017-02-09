package nl.lukasmiedema.locationquest.config

import nl.lukasmiedema.locationquest.security.DatabaseRememberMeServices
import nl.lukasmiedema.locationquest.security.PlayerAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * @author Lukas Miedema
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig : WebSecurityConfigurerAdapter(false) {

	private lateinit @Autowired var rememberMeServices : DatabaseRememberMeServices
	private lateinit @Autowired var authenticationProvider : PlayerAuthenticationProvider

	override fun configure(http: HttpSecurity) {
		// all resources are ok (method security is used on the controllers)
		http
				.csrf().disable()
				.headers().frameOptions().sameOrigin().and()
				.authorizeRequests().anyRequest().permitAll().and()
				.rememberMe().rememberMeServices(rememberMeServices)
	}

	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.authenticationProvider(this.authenticationProvider)
	}

}