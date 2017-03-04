package nl.lukasmiedema.locationquest.dto

import org.hibernate.validator.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Lukas Miedema
 */
open class LoginFormDto {

	@NotNull(message = "{login.error.noEmail}")
	@Email(message = "{login.error.invalidEmail}")
	var email: String? = null

	@NotNull(message = "{login.error.noPassword}")
	var password: String? = null
}