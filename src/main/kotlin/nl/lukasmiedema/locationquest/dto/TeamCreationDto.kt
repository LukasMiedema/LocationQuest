package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * @author Lukas Miedema
 */
open class TeamCreationDto {

	@NotNull(message = "Er is geen kleur opgegeven")
	@Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "\${validatedValue} is geen HTML kleur")
	var color: String? = null

	@NotNull(message = "Er is geen naam opgegeven")
	@Size(min = 3, max = 10, message = "Teamnaam moet tussen {min} en {max} tekens lang zijn")
	var name: String? = null

	val colorInt: Int?
		get() {
			return Integer.parseInt((this.color ?: return null).removePrefix("#"), 16)
		}
}