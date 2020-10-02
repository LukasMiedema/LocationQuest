package nl.lukasmiedema.locationquest.controller

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import nl.lukasmiedema.locationquest.dao.FileDao
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
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
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

/**
 * Files hosted by the application.
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("file")
class FileController {

	@Autowired private lateinit var fileDao: FileDao
	@Autowired private lateinit var environment: Environment

	@ResponseBody
	@GetMapping("/{fileId}")
	fun getFile(@PathVariable("fileId") fileId: UUID): ResponseEntity<ByteArray> {
		val file = fileDao.getFile(fileId)?: throw ResourceNotFoundException("No file for $fileId")

		val headers = HttpHeaders()
		if (file.mimeType != null) {
			headers.contentType = MediaType.parseMediaType(file.mimeType)
		}
		return ResponseEntity(file.file, headers, HttpStatus.OK)
	}

	@ResponseBody
	@GetMapping("/qr/games/{gameId}/secret/{secret}")
	fun getQrCode(@PathVariable("gameId") gameId: String, @PathVariable("secret") secret: String): ResponseEntity<ByteArray> {
		val url = "${environment["locationquest.domain"]}/s/${gameId}/${secret}"
		val properties = mapOf(
				EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
				EncodeHintType.MARGIN to 0)
		val matrix = MultiFormatWriter()
				.encode(url, BarcodeFormat.QR_CODE, 2048, 2048, properties)
		val image = MatrixToImageWriter.toBufferedImage(matrix)
		val outputStream = ByteArrayOutputStream()
		ImageIO.write(image, "png", outputStream)
		val headers = HttpHeaders()
		headers.contentType = MediaType.IMAGE_PNG
		return ResponseEntity(outputStream.toByteArray(), headers, HttpStatus.OK)
	}
}
