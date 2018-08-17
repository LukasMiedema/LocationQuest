package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

/**
 * @author Lukas Miedema
 */
class LoginFormDto {

	@NotNull(message = "{login.error.noEmail}")
	@Email(message = "{login.error.invalidEmail}")
	var email: String? = null

	@NotNull(message = "{login.error.noPassword}")
	var password: String? = null
}
