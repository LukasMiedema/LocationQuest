package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.Chapter
import java.beans.ConstructorProperties
import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
data class ChapterDto(val gameId: Int, val chapterId: Int, val color: Int, val name: String, val claimed: Boolean) {
	constructor(chapter: Chapter) : this(chapter.gameId, chapter.chapterId, chapter.color, chapter.name, false)

	val colorHex: String?
		get() = color.let { String.format("%06X", 0xFFFFFF and it) }
}
