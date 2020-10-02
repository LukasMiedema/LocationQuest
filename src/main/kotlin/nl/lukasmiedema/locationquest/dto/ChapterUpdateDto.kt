package nl.lukasmiedema.locationquest.dto

/**
 * @author Lukas Miedema
 */
class ChapterUpdateDto {
	var name: String? = null
	var colorHex: String? = null

	val color: Int?
		get() = colorHex?.let { Integer.parseInt(it.trim('#'), 16) }

}
