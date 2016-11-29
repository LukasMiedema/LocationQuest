package nl.lukasmiedema.locationquest.resource

import nl.lukasmiedema.locationquest.LocationQuestApplication
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import javax.validation.Valid

/**
 * @author Lukas Miedema
 */
@RestController
@Transactional
@RequestMapping(LocationQuestApplication.REST_PREFIX + "players/me")
open class PlayerResource {

	@Autowired private lateinit var sql: DSLContext;

	@RequestMapping("", method = arrayOf(RequestMethod.GET))
	open fun getMe(@SessionAttribute(required = false) httpSession: HttpSession?):Player =
			if (httpSession == null)
				throw ResourceNotFoundException("No player")
			else httpSession.getAttribute("player") as Player

	@RequestMapping("", method = arrayOf(RequestMethod.PUT))
	@ResponseStatus(code = HttpStatus.CREATED)
	open fun putPlayer(@RequestBody player: Player, @SessionAttribute(required = true) httpSession: HttpSession): Player {
		val session = httpSession.getAttribute("player") as Player
		session.name = player.name;
		return session;
	}
}