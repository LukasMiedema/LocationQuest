package nl.lukasmiedema.locationquest.dto

import java.beans.ConstructorProperties
import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
data class ChapterDto

	@ConstructorProperties("game_id", "chapter_id", "color", "name", "claimed")
	constructor(val gameId: Int, val chapterId: Int, val color: Int, val name: String, val claimed: Boolean) {

	val colorHex: String?
		get() = color.let { String.format("%06X", 0xFFFFFF and it) }
}