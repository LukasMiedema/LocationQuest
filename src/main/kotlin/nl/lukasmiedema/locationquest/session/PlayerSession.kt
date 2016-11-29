package nl.lukasmiedema.locationquest.session

import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import org.springframework.session.ExpiringSession
import org.springframework.session.Session
import java.util.*

/**
 * @author Lukas Miedema
 */
open class PlayerSession(val player: Player) : ExpiringSession {

	companion object {
		val PLAYER_ATTRIBUTE = "player";
	}

	constructor() : this(Player(UUID.randomUUID(), null)) {}

	override fun getCreationTime(): Long = -1;
	override fun getLastAccessedTime(): Long = -1;
	override fun setLastAccessedTime(lastAccessedTime: Long) = Unit
	override fun getMaxInactiveIntervalInSeconds() = -1;
	override fun isExpired() = false;
	override fun setMaxInactiveIntervalInSeconds(interval: Int) = Unit

	override fun getId(): String? = player.sessionId.toString()

	override fun removeAttribute(attributeName: String?) = Unit

	override fun getAttributeNames(): MutableSet<String> = hashSetOf(PLAYER_ATTRIBUTE)

	@Suppress("UNCHECKED_CAST")
	override fun <T> getAttribute(attributeName: String?): T? =
			if (attributeName == PLAYER_ATTRIBUTE) this.player as T else null

	override fun setAttribute(attributeName: String?, value: Any?) = Unit;
}