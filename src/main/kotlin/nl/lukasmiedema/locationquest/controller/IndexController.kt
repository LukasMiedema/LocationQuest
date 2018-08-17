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
class IndexController {

	@RequestMapping
	fun handleIndex(@AuthenticationPrincipal player: Player?): String =
			if (player == null) "redirect:/profile" else "redirect:/games"
}
