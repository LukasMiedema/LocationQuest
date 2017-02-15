package nl.lukasmiedema.locationquest.dto

import org.springframework.format.annotation.DateTimeFormat
import java.sql.Timestamp
import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
class GameInfoDto {

	// Properties from Game
	@Column(name = "GAME_ID")
	var gameId: Int? = null;

	@Column(name = "NAME")
	var name: String? = null;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "TIMESTAMP")
	var timestamp: Timestamp? = null;

	@Column(name = "ACTIVE")
	var active: Boolean? = null;

	@Column(name = "ALLOW_NEW_MEMBERS")
	var allowNewMembers: Boolean? = null;


	// Linked properties
	@Column(name = "TEAM_NAME")
	var teamName: String? = null;

	@Column(name = "TEAM_COLOR")
	var teamColor: Int? = null;

	/**
	 * Returns the team color as a hex string (i.e. ff00bb, without #)
	 */
	val teamColorHex: String?;
		get() = teamColor?.let { String.format("%06X", 0xFFFFFF and it) }
}