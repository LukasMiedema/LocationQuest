package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dto.ChooseNameFormDto
import nl.lukasmiedema.locationquest.dto.PageDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.security.PlayerAuthentication
import org.jooq.DSLContext
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.RememberMeServices
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


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

	@Autowired private lateinit var sql: DSLContext
	@Autowired private lateinit var authenticationManager: AuthenticationManager
	@Autowired private lateinit var rememberMeServices: RememberMeServices

	@GetMapping
	open fun getChooseName(
			@ModelAttribute("page") page: PageDto,
			@ModelAttribute("form") form: ChooseNameFormDto): String {
		return PROFILE_VIEW
	}

	@PostMapping
	open fun postChooseName(
			servletRequest: HttpServletRequest,
			servletResponse: HttpServletResponse,
			@Valid @ModelAttribute("form") chooseNameFormDto: ChooseNameFormDto,
			bindingResult: BindingResult,
			@AuthenticationPrincipal principal: Player?): String {

		// Check if valid
		if (bindingResult.hasErrors()) {
			return PROFILE_VIEW
		}

		// Check if this is a rename or a registration
		if (principal == null) {
			// Login / register

			// Save the user details
			val p = Tables.PLAYER
			val player: Player = sql
					.insertInto(p, p.NAME)
					.values(chooseNameFormDto.name)
					.returning(*p.fields())
					.fetchOne().into(Player::class.java)

			// Authenticate
			val auth = this.authenticationManager.authenticate(PlayerAuthentication(player))
			SecurityContextHolder.getContext().authentication = auth
			rememberMeServices.loginSuccess(servletRequest, servletResponse, auth)

		} else {

			// Rename
			principal.name = chooseNameFormDto.name
			val p = Tables.PLAYER
			sql
					.update(p)
					.set(p.NAME, principal.name)
					.where(p.PLAYER_ID.eq(principal.playerId))
					.execute()
		}

		// Redirect to the games page
		return "redirect:/${GamesController.URL}"
	}
}