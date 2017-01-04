package nl.lukasmiedema.locationquest.dto

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
class TeamInfoDto {

	@Column(name = "ID")
	var id: Int? = null;

	@Column(name = "NAME")
	var name: String? = null

	@Column(name = "COLOR")
	var color: Int? = null

	@Column(name = "MEMBER_COUNT")
	var memberCount: Int? = null

	/**
	 * Returns the team color as a hex string (i.e. ff00bb, without #)
	 */
	val colorHex: String?;
		get() = color?.let { String.format("%06X", 0xFFFFFF and it) }
}