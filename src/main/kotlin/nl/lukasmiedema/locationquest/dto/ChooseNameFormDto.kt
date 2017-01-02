package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Lukas Miedema
 */
class ChooseNameFormDto() {

	@NotNull
	@Size(min = 3, max = 10, message = "Naam moet tussen {min} en {max} tekens lang zijn")
	var name: String? = null

	override fun toString(): String {
		return "ChooseNameFormDto(name=$name)"
	}
}