package nl.lukasmiedema.locationquest.session

import nl.lukasmiedema.locationquest.LocationQuestApplication
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.session.ExpiringSession
import org.springframework.session.Session
import org.springframework.session.SessionRepository
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

	@Autowired private lateinit var sql: DSLContext;

	override fun createSession(): PlayerSession = PlayerSession()

	override fun save(session: ExpiringSession) : Unit {
		val player = (session as PlayerSession).player;
		if (player.name == null) return
		sql
				.insertInto(Tables.PLAYER, Tables.PLAYER.NAME, Tables.PLAYER.SESSION_ID)
				.values(player.name, player.sessionId)
				.onDuplicateKeyIgnore()
				.execute();
	}

	override fun delete(id: String?) = Unit

	override fun getSession(id: String?): PlayerSession? {
		val uuid = UUID.fromString(id)
		val player : Player? = sql
				.selectFrom(Tables.PLAYER)
				.where(Tables.PLAYER.SESSION_ID.eq(uuid))
				.fetchAnyInto(Player::class.java)
		return if (player == null) null else PlayerSession(player)
	}
}