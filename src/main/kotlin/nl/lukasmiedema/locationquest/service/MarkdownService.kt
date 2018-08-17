package nl.lukasmiedema.locationquest.service

import org.springframework.stereotype.Service
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.ParserEmulationProfile
import com.vladsch.flexmark.util.options.MutableDataSet

/**
 * Converts markdown strings to HTML strings safe for viewing by the user.
 * @author Lukas Miedema
 */
@Service
class MarkdownService {

	private val parser: Parser
	private val htmlRenderer: HtmlRenderer

	init {
		val options = MutableDataSet()
		options.setFrom(ParserEmulationProfile.COMMONMARK)

		this.parser = Parser.builder(options).build()

		val htmlRendererBuilder = HtmlRenderer.builder(options)
		htmlRendererBuilder.escapeHtml(true)
		htmlRendererBuilder.softBreak("<br/>")

		this.htmlRenderer = htmlRendererBuilder.build()
	}

	fun toHtml(markdown: String): String {
		return this.htmlRenderer.render(this.parser.parse(markdown))
	}
}
