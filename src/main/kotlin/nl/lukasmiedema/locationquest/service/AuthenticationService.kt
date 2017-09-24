package nl.lukasmiedema.locationquest.service

import nl.lukasmiedema.locationquest.security.PlayerAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
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
open class AuthenticationService {

	@Autowired private lateinit var authenticationManager: AuthenticationManager
	@Autowired private lateinit var rememberMeServices: RememberMeServices

	/**
	 * Authenticate the current context.
	 */
	open fun authenticate(req: HttpServletRequest, resp: HttpServletResponse, auth: PlayerAuthentication) {
		val authenticatedAuth = this.authenticationManager.authenticate(auth)
		SecurityContextHolder.getContext().authentication = authenticatedAuth
		rememberMeServices.loginSuccess(req, resp, authenticatedAuth)
	}

}