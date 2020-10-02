package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Make the QR code URL shorter
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/s/{gameId}/{scanCode}")
class ScanForwardController {

	@Autowired
	private lateinit var questDao: QuestDao

	@GetMapping
	fun scan(@PathVariable("gameId") gameId: Int, @PathVariable("scanCode") scanCode: String): String {
		return "redirect:/games/${gameId}/scan/${scanCode}"
	}
}
