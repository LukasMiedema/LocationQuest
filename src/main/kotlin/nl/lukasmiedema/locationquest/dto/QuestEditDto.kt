package nl.lukasmiedema.locationquest.dto

import java.util.*

/**
 * @author Lukas Miedema
 */
class QuestEditDto {

	var chapterId: Int? = null
	var name: String? = null
	var fetchText: String? = null
	var scanText: String? = null
	var required: Boolean = true
	var qrCode: String? = null
	var passcodeText: String? = null
}
