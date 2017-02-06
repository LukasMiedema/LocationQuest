package nl.lukasmiedema.locationquest.dto.quest

/**
 * @author Lukas Miedema
 */
enum class QuestPhase(val htmlClass: String, val icon: String) {

	/**
	 * Display a multiple choice question.
	 */
	MULTIPLE_CHOICE("blue", "question"),

	/**
	 * A text is displayed which should guide the players to a location where a QR code can be scanned
	 */
	QR_TEXT_FETCH("teal", "qrcode"),

	/**
	 * No more quests
	 */
	DONE("green", "flag checkered"),

	/**
	 * The game is closed.
	 */
	CLOSED("orange", "lock")
}