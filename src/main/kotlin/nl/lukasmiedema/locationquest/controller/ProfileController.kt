package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.PlayerDao
import nl.lukasmiedema.locationquest.dto.ChooseNameFormDto
import nl.lukasmiedema.locationquest.dto.PageDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.security.PlayerAuthentication
import nl.lukasmiedema.locationquest.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


/**
 * Controls the choose name page.
 * @author Lukas Miedema
 */
@Controller
@RequestMapping("profile")
class ProfileController {

	@Autowired private lateinit var playerDao: PlayerDao
	@Autowired private lateinit var authenticationService: AuthenticationService

	@GetMapping
	fun getChooseName(@ModelAttribute("form") form: ChooseNameFormDto): String {
		return "Profile"
	}

	@PostMapping
	fun postChooseName(
			servletRequest: HttpServletRequest,
			servletResponse: HttpServletResponse,
			@Valid @ModelAttribute("form") chooseNameFormDto: ChooseNameFormDto,
			bindingResult: BindingResult,
			@AuthenticationPrincipal principal: Player?): String {

		// Check if valid
		if (bindingResult.hasErrors()) {
			return "Profile"
		}

		// Check if this is a rename or a registration
		if (principal == null) {
			// Login / register

			// Save the user details
			val player = playerDao.createNewPlayer(chooseNameFormDto.name!!)

			// Authenticate
			authenticationService.authenticate(servletRequest, servletResponse, PlayerAuthentication(player = player))

		} else {

			// Rename
			principal.name = chooseNameFormDto.name
			playerDao.renamePlayer(principal.playerId, principal.name)
		}

		// Redirect to the games page
		return "redirect:/games"
	}
}
