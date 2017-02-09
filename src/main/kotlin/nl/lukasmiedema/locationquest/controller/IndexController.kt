package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 */
@Controller
@RequestMapping("/")
open class IndexController {

	@RequestMapping
	open fun handleIndex(@AuthenticationPrincipal player: Player?): String {
		// redirect to something
		if (player == null) {
			return "redirect:/${ProfileController.URL}"
		} else {
			return "redirect:/${GamesController.URL}"
		}
	}
}