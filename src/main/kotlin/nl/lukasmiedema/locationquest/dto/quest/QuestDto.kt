package nl.lukasmiedema.locationquest.dto.quest

import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
abstract class QuestDto(val title: String) {

	/**
	 * The current phase
	 */
	abstract val phase: QuestPhase
}