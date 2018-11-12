package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.PlayerDao
import nl.lukasmiedema.locationquest.dto.MessageDto
import nl.lukasmiedema.locationquest.dto.RegisterFormDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.entity.tables.pojos.PlayerCredentials
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

/**
 * Lets you set a password.
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("register")
class RegisterController {

	@Autowired private lateinit var playerDao: PlayerDao
	@Autowired private lateinit var passwordEncoder: PasswordEncoder

	@GetMapping
	fun getLogin(
			@AuthenticationPrincipal player: Player,
			@ModelAttribute("form") form: RegisterFormDto): String {
		val credentials = playerDao.getCredentials(player.playerId)
		if (credentials != null) {
			form.email = credentials.emailAddress;
		}
		return "Register"
	}

	@PostMapping
	fun postLogin(
			@AuthenticationPrincipal player: Player,
			@Valid @ModelAttribute("form") form: RegisterFormDto,
			bindingResult: BindingResult,
			redirectAttributes: RedirectAttributes): String {

		if (!bindingResult.hasErrors()) {
			val duplicateCredentials = playerDao.getCredentialsByEmail(form.email!!)
			if (duplicateCredentials != null && duplicateCredentials.playerId != player.playerId) {
				bindingResult.rejectValue("email", "register.error.emailAlreadyInUse")
			}
		}

		if (!bindingResult.hasErrors()) {
			val playerCredentials = PlayerCredentials()
			playerCredentials.playerId = player.playerId
			playerCredentials.emailAddress = form.email
			playerCredentials.passwordHash = passwordEncoder.encode(form.password)
			playerCredentials.playerId = player.playerId
			playerDao.setPlayerCredentials(playerCredentials)

			val message = MessageDto(MessageDto.MessageType.SUCCESS, "Credentials set")
			redirectAttributes.addFlashAttribute("messages", listOf(message))
			return "redirect:/"
		}
		return "Register"
	}
}
