package nl.lukasmiedema.locationquest.dto.quest

import javax.persistence.Column

/**
 * @author Lukas Miedema
 */
interface QuestDto {

	/**
	 * The current phase
	 */
	val phase: QuestPhase
}