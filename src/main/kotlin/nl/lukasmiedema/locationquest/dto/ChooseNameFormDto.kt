package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Lukas Miedema
 */
class ChooseNameFormDto() {

	@NotNull(message = "{profile.error.noName}")
	@Size(min = 3, max = 10, message = "{profile.error.nameLength}")
	var name: String? = null

	override fun toString(): String {
		return "ChooseNameFormDto(name=$name)"
	}
}