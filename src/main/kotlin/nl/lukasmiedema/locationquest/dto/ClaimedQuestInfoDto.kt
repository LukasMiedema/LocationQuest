package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.ClaimedQuest
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Timestamp

/**
 * Every bit of detail about a claimed quest.
 * @author Lukas Miedema
 */
data class ClaimedQuestInfoDto(
		val chapter: ChapterDto,
		val claimedQuest: ClaimedQuest,
		val quest: Quest) {

	/**
	 * Claimed at annotated with a datetime format for Thymeleaf.
	 */
	val claimedAt: Timestamp
		@DateTimeFormat(pattern = "HH:mm")
		get() = claimedQuest.claimedAt
}