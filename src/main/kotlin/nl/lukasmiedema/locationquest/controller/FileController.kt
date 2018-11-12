package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.FileDao
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

/**
 * Files hosted by the application.
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("file/{fileId}")
class FileController {

	@Autowired private lateinit var fileDao: FileDao

	@ResponseBody
	@GetMapping
	fun getFile(@PathVariable("fileId") fileId: UUID): ResponseEntity<ByteArray> {
		val file = fileDao.getFile(fileId)?: throw ResourceNotFoundException("No file for $fileId")

		val headers = HttpHeaders()
		if (file.mimeType != null) {
			headers.contentType = MediaType.parseMediaType(file.mimeType)
		}
		return ResponseEntity(file.file, headers, HttpStatus.OK)
	}

}
