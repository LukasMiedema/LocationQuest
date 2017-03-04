package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.PlayerDao
import nl.lukasmiedema.locationquest.dto.ChooseNameFormDto
import nl.lukasmiedema.locationquest.dto.LoginFormDto
import nl.lukasmiedema.locationquest.dto.PageDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.security.PlayerAuthentication
import nl.lukasmiedema.locationquest.service.AuthenticationService
import nl.lukasmiedema.locationquest.service.I18nService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


/**
 * Lets you login.
 * @author Lukas Miedema
 */
@Controller
@RequestMapping("login")
open class LoginController {

	@Autowired private lateinit var playerDao: PlayerDao
	@Autowired private lateinit var authenticationService: AuthenticationService
	@Autowired private lateinit var i18n: I18nService
	@Autowired private lateinit var passwordEncoder: PasswordEncoder

	@GetMapping
	open fun getLogin(@ModelAttribute("form") form: LoginFormDto): String {
		return "Login"
	}

	@PostMapping
	open fun postLogin(
			servletRequest: HttpServletRequest,
			servletResponse: HttpServletResponse,
			@Valid @ModelAttribute("form") form: LoginFormDto,
			bindingResult: BindingResult): String {

		// Check if valid
		if (!bindingResult.hasErrors()) {

			// Retrieve credentials
			val credentials = playerDao.getCredentials(form.email!!)
			if (credentials != null && passwordEncoder.matches(form.password, credentials.passwordHash)) {

				// Login
				val player = playerDao.getPlayer(credentials.playerId)!!
				authenticationService.authenticate(servletRequest, servletResponse, PlayerAuthentication(player))
				return "redirect:/games"

			} else {
				bindingResult.addError(ObjectError("email", i18n["login.error.credentials"]))
			}
		}

		// Errors, show the page again
		return "Login"
	}
}