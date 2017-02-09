package nl.lukasmiedema.locationquest.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

/**
 * @author Lukas Miedema
 */
@Component
open class PlayerAuthenticationProvider : AuthenticationProvider {

	override fun authenticate(authentication: Authentication): Authentication? {
		if (authentication is PlayerAuthentication) {
			authentication.isAuthenticated = true
			return authentication
		}
		return null
	}

	override fun supports(authentication: Class<*>?): Boolean = authentication == PlayerAuthentication::class.java
}