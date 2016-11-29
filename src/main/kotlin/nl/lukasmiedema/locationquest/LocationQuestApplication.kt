package nl.lukasmiedema.locationquest

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class LocationQuestApplication {
	companion object {

		/**
		 * Prefix used by all REST endpoints. This is a compile-time static, so changing this requires recompilation
		 * of the whole application (it's used in annotations)
		 */
		const val REST_PREFIX = "/api/v1/";

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