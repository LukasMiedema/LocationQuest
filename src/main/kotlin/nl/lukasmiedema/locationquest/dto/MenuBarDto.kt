package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.Game

/**
 * @author Lukas Miedema
 */
data class MenuBarDto(
		val loggedIn: Boolean,
		val name: String?,
		val games: List<Game>?
)