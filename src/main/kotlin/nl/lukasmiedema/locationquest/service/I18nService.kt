package nl.lukasmiedema.locationquest.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

/**
 * Simple wrap around {@link MessageSource} but a little easier to use.
 * @author Lukas Miedema
 */
@Service
open class I18nService {

	@Autowired private lateinit var messageSource: MessageSource

	/**
	 * Returns the translated string associated with the code code.
	 */
	operator open fun get(code: String, vararg params: Any): String {
		return messageSource.getMessage(code, params, LocaleContextHolder.getLocale())
	}
}
