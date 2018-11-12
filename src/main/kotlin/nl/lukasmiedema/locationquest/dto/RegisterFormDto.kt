package nl.lukasmiedema.locationquest.dto

import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

/**
 * @author Lukas Miedema
 */
class RegisterFormDto {

	@NotNull(message = "{register.error.noEmail}")
	@Email(message = "{register.error.invalidEmail}")
	var email: String? = null

	@NotNull(message = "{register.error.noPassword}")
	var password: String? = null

	@NotNull(message = "{register.error.noRepeatPassword}")
	var repeatPassword: String? = null

	@AssertTrue(message = "{register.error.passwordMismatch}")
	fun isPasswordMatch() = password == repeatPassword
}
