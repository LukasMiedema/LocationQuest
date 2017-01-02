package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.ChooseNameFormDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.session.PlayerSessionRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpSession
import javax.validation.Valid

/**
 * Controls the choose name page.
 * @author Lukas Miedema
 */
@Controller
@RequestMapping(ProfileController.URL)
open class ProfileController {

	companion object {
		const val URL = "profile"
		const val PROFILE_VIEW = "Profile"
	}

	@GetMapping
	open fun getChooseName(model: Model, session: HttpSession): String {
		model.addAttribute("title", "Kies een naam")
		model.addAttribute("chooseNameForm", ChooseNameFormDto())
		return PROFILE_VIEW
	}

	@PostMapping
	open fun postChooseName(
			@Valid @ModelAttribute("chooseNameForm") chooseNameForm: ChooseNameFormDto,
			bindingResult: BindingResult, model: Model,	@AuthenticationPrincipal principal: Player?): String {

		// Check if valid
		if (bindingResult.hasErrors()) {
			model.addAttribute("title", "Kies een naam")
			model.addAttribute("chooseNameForm", chooseNameForm)
			return PROFILE_VIEW
		}

		// Save the user details
		val player = principal ?: Player(UUID.randomUUID(), null)
		player.name = chooseNameForm.name
		SecurityContextHolder.getContext().authentication = PlayerSessionRepository.SessionAuthentication(player)
		return "redirect:/${GamesController.URL}"
	}
}