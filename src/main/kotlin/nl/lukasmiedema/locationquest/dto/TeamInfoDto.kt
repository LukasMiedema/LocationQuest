package nl.lukasmiedema.locationquest.dto

import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
class TeamInfoDto {

	@Column(name = "TEAM_ID")
	var teamId: Int? = null;

	@Column(name = "NAME")
	var name: String? = null

	@Column(name = "COLOR")
	var color: Int? = null

	@Column(name = "MEMBER_COUNT")
	var memberCount: Int? = null

	/**
	 * Returns the team color as a hex string (i.e. ff00bb, without #)
	 */
	val colorHex: String?
		get() = color?.let { String.format("%06X", 0xFFFFFF and it) }
}