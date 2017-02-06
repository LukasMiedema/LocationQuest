package nl.lukasmiedema.locationquest.dto.quest

/**
 * @author Lukas Miedema
 */
class FetchQuestDto(val title: String, val text: String) : QuestDto {

	override val phase: QuestPhase
		get() = QuestPhase.QR_TEXT_FETCH

}