package nl.lukasmiedema.locationquest.security

import org.springframework.security.core.GrantedAuthority

enum class Authority : GrantedAuthority {
	ADMIN, PARTICIPANT;

	override fun getAuthority(): String = "ROLE_" + name

	companion object {
		val ADMIN_AUTHORITIES = listOf(ADMIN, PARTICIPANT)
		val PLAYER_AUTHORITIES = listOf(PARTICIPANT)
	}
}