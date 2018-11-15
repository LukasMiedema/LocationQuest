package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.entity.tables.pojos.QuestAnswer

/**
 * @author Lukas Miedema
 */
class QuestDto : Quest() {

	/**
	 * A non-empty list indicates this quest requires a multiple-choice question to be answered before
	 * claiming. The answer to that question is only stored and not checked.
	 */
	var answers = emptyList<QuestAnswer>()
}
