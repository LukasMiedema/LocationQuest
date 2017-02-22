package nl.lukasmiedema.locationquest.dto

import java.beans.ConstructorProperties
import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
data class ChapterDto
	@ConstructorProperties("GAME_ID", "CHAPTER_ID", "COLOR", "CLAIMED")
	constructor(val gameId: Int, val chapterId: Int, val color: Int, val claimed: Boolean) {

	val colorHex: String?;
		get() = color.let { String.format("%06X", 0xFFFFFF and it) }
}