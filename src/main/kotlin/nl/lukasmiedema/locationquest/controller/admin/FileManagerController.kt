package nl.lukasmiedema.locationquest.controller.admin

import nl.lukasmiedema.locationquest.dao.FileDao
import nl.lukasmiedema.locationquest.dto.FileEditDto
import nl.lukasmiedema.locationquest.dto.LoginFormDto
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.service.I18nService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

/**
 * @author Lukas Miedema
 */
@Controller
@RequestMapping("admin/files")
class FileManagerController {

	@Autowired private lateinit var i18n: I18nService
	@Autowired private lateinit var fileDao: FileDao

	@GetMapping
	fun getList(
			model: Model): String {

		// Get all files in the system
		val files = fileDao.getFiles()
		model.addAttribute("files", files)

		return "FileManager"
	}

	@GetMapping("/new")
	fun getEditorNew(model: Model): String {
		val fileDto = FileEditDto()
		fileDto.uuid = UUID.randomUUID()
		model.addAttribute("file", fileDto)

		return "FileEditor"
	}

	@PostMapping("/new")
	fun postEditorNew(
			@Valid @ModelAttribute("file") file: FileEditDto,
			bindingResult: BindingResult,
			model: Model): String {

		// Check if valid
		if (!bindingResult.hasErrors()) {
			val fileData = file.file
			if (fileData == null || fileData.isEmpty) {

				// A file is required for the initial upload
				bindingResult.addError(ObjectError("file", i18n["fileManager.editor.error.noFile"]))

			} else {

				// Insert
				fileDao.insertFile(file.uuid!!, file.mimeType!!, fileData.bytes)
				return "redirect:/admin/files/${file.uuid}"
			}
		}

		return "FileEditor"
	}

	@GetMapping("/{fileId}")
	fun getEditor(
			@PathVariable("fileId") fileId: UUID,
			model: Model): String {

		// Get the file from database
		val file = fileDao.getFile(fileId)?: throw ResourceNotFoundException("No such file")

		// Insert into dto
		val fileDto = FileEditDto()
		fileDto.mimeType = file.mimeType
		fileDto.uuid = file.fileId

		model.addAttribute("file", fileDto)
		model.addAttribute("")
		return "FileEditor"
	}

	@PostMapping("/{fileId}")
	fun postEditor(
			@PathVariable("fileId") fileId: UUID,
			@Valid @ModelAttribute("file") file: FileEditDto,
			bindingResult: BindingResult,
			model: Model): String {

		// Check if valid
		if (!bindingResult.hasErrors()) {
			val fileData = file.file
			if (fileData == null || fileData.isEmpty) {
				// Just update the metadata
				fileDao.updateFile(fileId, file.uuid!!, file.mimeType!!)
			} else {
				// Full replace
				fileDao.updateFile(fileId, file.uuid!!, file.mimeType!!, fileData.bytes)
			}

			if (file.uuid != fileId) {
				// Renamed -> redirect
				return "redirect:/admin/files/${file.uuid}"
			}
		}

		return "FileEditor"
	}
}
