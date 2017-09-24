package nl.lukasmiedema.locationquest.service

import nl.lukasmiedema.locationquest.security.PlayerAuthentication
import nl.lukasmiedema.locationquest.security.PlayerAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Utility class to programmatically log players in.
 * @author Lukas Miedema
 */
@Service
class AuthenticationService @Autowired constructor(
		val provider: PlayerAuthenticationProvider,
		val rememberMeServices: RememberMeServices) {

	/**
	 * Authenticate the current context.
	 */
	fun authenticate(req: HttpServletRequest, resp: HttpServletResponse, auth: PlayerAuthentication) {
		val authenticatedAuth = this.provider.authenticate(auth)
		SecurityContextHolder.getContext().authentication = authenticatedAuth
		rememberMeServices.loginSuccess(req, resp, authenticatedAuth)
	}

}