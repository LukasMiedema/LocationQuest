package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.NotNull

/**
 * @author Lukas Miedema
 */
class TeamSelectionDto {

	@NotNull(message = "{enroll.choose.error.noTeam}")
	var teamId: Int? = null;
}