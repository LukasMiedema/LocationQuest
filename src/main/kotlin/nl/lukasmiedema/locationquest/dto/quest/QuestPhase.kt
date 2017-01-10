package nl.lukasmiedema.locationquest.dto.quest

/**
 * @author Lukas Miedema
 */
enum class QuestPhase {

	/**
	 * This quest is in the "question" phase. Display a multiple choice question.
	 */
	PUZZLE,

	/**
	 * This quest is in the "timeout" phase and no action may be performed.
	 */
	TIMEOUT,

	/**
	 * This quest is in the fetch phase where a QR code may be scanned and a story is shown.
	 */
	FETCH,

	/**
	 * No more quests
	 */
	DONE,

	/**
	 * The game is closed.
	 */
	CLOSED
}