package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.NotNull

/**
 * @author Lukas Miedema
 */
class MultipleChoiceAnswerDto {

	@NotNull(message = "Kies eerst een antwoord")
	var option: Int? = null
}