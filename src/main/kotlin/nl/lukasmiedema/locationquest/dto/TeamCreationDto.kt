package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * @author Lukas Miedema
 */
class TeamCreationDto {

	@NotNull(message = "{enroll.create.error.noColor}")
	@Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "{enroll.create.error.invalidColor}")
	var color: String? = null

	@NotNull(message = "{enroll.create.error.noName}")
	@Size(min = 3, max = 10, message = "{enroll.create.error.nameLength}")
	var name: String? = null

	val colorInt: Int?
		get() {
			return Integer.parseInt((this.color ?: return null).removePrefix("#"), 16)
		}
}
