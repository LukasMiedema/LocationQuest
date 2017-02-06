package nl.lukasmiedema.locationquest.dto

/**
 * @author Lukas Miedema
 */
class MessageDto(val type: MessageType,	val message: String) {

	/**
	 * Semantic UI notification type
	 */
	enum class MessageType(val htmlClass: String) {
		INFO("info"),
		WARNING("warning"),
		SUCCESS("positive"),
		ERROR("negative"),
		NOTHING("")
	}
}