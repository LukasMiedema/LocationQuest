package nl.lukasmiedema.locationquest.security

import nl.lukasmiedema.locationquest.LocationQuestApplication
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
open class DatabaseRememberMeServices : RememberMeServices {

	private lateinit @Autowired var sql: DSLContext

	override fun loginSuccess(
			request: HttpServletRequest,
			response: HttpServletResponse,
			successfulAuthentication: Authentication) {

		// Get the player out
		val player = successfulAuthentication.principal as Player

		// Assign a new token and store
		val token = UUID.randomUUID()
		val pt = Tables.PLAYER_TOKEN
		sql
				.insertInto(pt, pt.PLAYER_ID, pt.TOKEN)
				.values(player.playerId, token)
				.execute()

		// Create a cookie and set this
		val cookie = Cookie(LocationQuestApplication.Companion.COOKIE_NAME, token.toString())
		cookie.maxAge = Int.MAX_VALUE

		response.addCookie(cookie)
	}

	override fun autoLogin(request: HttpServletRequest, response: HttpServletResponse): Authentication? {

		// Get the token from the cookie
		val cookie: Cookie = (request.cookies ?: emptyArray<Cookie>())
				.firstOrNull { it.name == LocationQuestApplication.COOKIE_NAME } ?: return null

		// Do a database search on this cookie
		val uuid = UUID.fromString(cookie.value)
		val p = Tables.PLAYER.`as`("p")
		val t = Tables.PLAYER_TOKEN.`as`("t")
		val player : Player = sql
				.select(*p.fields())
				.from(p.join(t).on(p.PLAYER_ID.eq(t.PLAYER_ID)))
				.where(t.TOKEN.eq(uuid))
				.fetchOneInto(Player::class.java) ?: return null

		// Authenticate the player
		return PlayerAuthentication(player)
	}

	override fun loginFail(request: HttpServletRequest?, response: HttpServletResponse?) = Unit

}