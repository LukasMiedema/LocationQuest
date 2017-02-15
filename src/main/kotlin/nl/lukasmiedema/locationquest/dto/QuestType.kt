package nl.lukasmiedema.locationquest.dto

/**
 * @author Lukas Miedema
 */
enum class QuestType(val htmlClass: String, val icon: String) {

	/**
	 * A text is displayed which should guide the players to a location where a QR code can be scanned
	 */
	QR_TEXT_FETCH("teal", "qrcode"),

	/**
	 * A passcode box is displayed
	 */
	PASSCODE("red", "archive"),

	/**
	 * No more quests
	 */
	DONE("green", "flag checkered"),

	/**
	 * The game is closed.
	 */
	CLOSED("orange", "lock")
}