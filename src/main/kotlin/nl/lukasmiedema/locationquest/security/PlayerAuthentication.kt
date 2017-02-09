package nl.lukasmiedema.locationquest.security

import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.security.Authority.Companion.ADMIN_AUTHORITIES
import nl.lukasmiedema.locationquest.security.Authority.Companion.PLAYER_AUTHORITIES
import org.springframework.security.authentication.AbstractAuthenticationToken

class PlayerAuthentication(val player: Player) :
		AbstractAuthenticationToken(if (player.systemAdmin) ADMIN_AUTHORITIES else PLAYER_AUTHORITIES) {

	override fun getCredentials() = ""
	override fun getPrincipal() = player
}