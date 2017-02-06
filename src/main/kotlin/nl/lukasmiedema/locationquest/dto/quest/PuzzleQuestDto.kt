package nl.lukasmiedema.locationquest.dto.quest

import nl.lukasmiedema.locationquest.entity.tables.pojos.MultipleChoiceAnswer

/**
 * @author Lukas Miedema
 */
class PuzzleQuestDto(
		val title: String,
		val text: String,
		val options: List<MultipleChoiceAnswer>
) : QuestDto {

	override val phase: QuestPhase
		get() = QuestPhase.MULTIPLE_CHOICE

}