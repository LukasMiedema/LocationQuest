package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.entity.tables.pojos.QuestAnswer
import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
class QuestDto : Quest() {

	@Column(name = "GAME_ID")
	var gameId: Int? = null

	/**
	 * A non-empty list indicates this quest requires a multiple-choice question to be answered before
	 * claiming. The answer to that question is only stored and not checked.
	 */
	var answers = emptyList<QuestAnswer>()
}
