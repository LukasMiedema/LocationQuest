package nl.lukasmiedema.locationquest

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class LocationQuestApplication {
	companion object {

		/**
		 * Name of the cookie
		 */
		const val COOKIE_NAME = "LocationQuestUUID";

		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(LocationQuestApplication::class.java, *args)
		}
	}
}