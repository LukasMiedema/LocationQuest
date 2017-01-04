package nl.lukasmiedema.locationquest.dto

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotNull

/**
 * @author Lukas Miedema
 */
class TeamSelectionDto {

	@NotNull(message = "Klik eerst een team aan")
	var teamId: Int? = null;
}