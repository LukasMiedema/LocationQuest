package nl.lukasmiedema.locationquest.dto.quest

import nl.lukasmiedema.locationquest.entity.tables.pojos.MultipleChoiceAnswer

/**
 * @author Lukas Miedema
 */
class PuzzleQuestDto(
		title: String,
		val text: String,
		val options: List<MultipleChoiceAnswer>
) : QuestDto(title) {

	override val phase: QuestPhase
		get() = QuestPhase.PUZZLE

}