package nl.lukasmiedema.locationquest.session

import nl.lukasmiedema.locationquest.LocationQuestApplication
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.entity.tables.records.PlayerRecord
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.session.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author Lukas Miedema
 */
@Component
@Transactional
open class PlayerSessionRepository : SessionRepository<ExpiringSession> {

	companion object {
		//val PLAYER_ATTRIBUTE = "player";
		val SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
	}

	@Autowired private lateinit var sql: DSLContext;
	@Autowired private lateinit var authenticationManager: AuthenticationManager;
	private val sessionRepository: SessionRepository<ExpiringSession> = MapSessionRepository()

	override fun createSession(): ExpiringSession = sessionRepository.createSession()

	override fun save(session: ExpiringSession) : Unit {
		sessionRepository.save(session)
		val context = session.getAttribute<SecurityContext?>(SPRING_SECURITY_CONTEXT) ?: return
		val player = (context.authentication as SessionAuthentication).player

		// Insert or update
		val playerRecord = sql.newRecord(Tables.PLAYER).values(player.sessionId, player.name)
		if (sql.fetchExists(DSL.selectFrom(Tables.PLAYER).where(Tables.PLAYER.SESSION_ID.eq(player.sessionId)))) {
			playerRecord.update()
		} else {
			playerRecord.insert()
		}
	}

	override fun delete(id: String?) = sessionRepository.delete(id)

	override fun getSession(id: String?) : ExpiringSession? {
		val session = sessionRepository.getSession(id)
		if (session != null) {
			return session;
		} else {

			val uuid = UUID.fromString(id)
			val player: Player = sql
					.selectFrom(Tables.PLAYER)
					.where(Tables.PLAYER.SESSION_ID.eq(uuid))
					.fetchAnyInto(Player::class.java) ?: return null

			val dbSession = MapSession(uuid.toString())

			// When the session comes from the database, Spring security needs to be made aware of this somehow
			// Therefore, create a context with the player as principal
			val context = SecurityContextHolder.createEmptyContext()
			context.authentication = SessionAuthentication(player)
			dbSession.setAttribute(SPRING_SECURITY_CONTEXT, context)

			// And return the new session
			return dbSession
		}
	}

	class SessionAuthentication(var player: Player) : AbstractAuthenticationToken(emptyList()) {

		init {
			isAuthenticated = true;
		}

		override fun getCredentials(): Any? = null
		override fun getPrincipal(): Any = player
	}
}