package nl.lukasmiedema.locationquest.controller.admin

import nl.lukasmiedema.locationquest.dao.GamesDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("admin")
class AdminController {

	@Autowired
	private lateinit var gamesDao: GamesDao

	@GetMapping()
	fun get(model: Model): String {
		val games = gamesDao.getGames()
		model.addAttribute("games", games)
		return "admin/Admin";
	}
}
