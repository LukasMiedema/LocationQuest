package nl.lukasmiedema.locationquest.dto.quest

/**
 * @author Lukas Miedema
 */
class FetchQuestDto(title: String, val text: String) : QuestDto(title) {

	override val phase: QuestPhase
		get() = QuestPhase.FETCH

}