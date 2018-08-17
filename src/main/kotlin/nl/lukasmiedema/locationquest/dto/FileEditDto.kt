package nl.lukasmiedema.locationquest.dto

import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.constraints.NotNull

/**
 * @author Lukas Miedema
 */
class FileEditDto {

	@NotNull
	var uuid: UUID? = null

	@NotNull
	var mimeType: String? = null

	var file: MultipartFile? = null
}
