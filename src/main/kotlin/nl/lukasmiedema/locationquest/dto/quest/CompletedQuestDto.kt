package nl.lukasmiedema.locationquest.dto.quest

/**
 * Holds no data other than what {@link QuestDto} holds.
 * @author Lukas Miedema
 */
class CompletedQuestDto(title: String, override val phase: QuestPhase) : QuestDto(title)