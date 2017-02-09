package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.Game

/**
 * @author Lukas Miedema
 */
data class PageDto(
		val loggedIn: Boolean,
		val name: String?,
		val games: List<Game>?
) {
	/**
	 * The title of the page
	 */
	var title: String = "Untitled"

	/*
	/**
	 * Additional styles to add to the page. URLs are relative to the styles folder.
	 */
	val styles: MutableList<String> = arrayListOf("main")

	/**
	 * Additional scripts to add to the page. URLs are relative to the scripts folder.
	 */
	val scripts: MutableList<String> = arrayListOf("main")*/
}