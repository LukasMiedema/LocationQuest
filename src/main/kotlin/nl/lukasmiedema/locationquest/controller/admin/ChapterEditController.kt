package nl.lukasmiedema.locationquest.controller.admin

import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.ChapterDto
import nl.lukasmiedema.locationquest.dto.ChapterUpdateDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Chapter
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 * Update chapters. All modification happen from the /quests page.
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("admin/games/{gameId}/chapters")
class ChapterEditController {

	@Autowired private lateinit var questDao: QuestDao

	@PostMapping("/{chapterId}", "/new")
	fun postChapter(@PathVariable gameId: Int,
					@PathVariable(required = false) chapterId: Int?,
					chapterDto: ChapterUpdateDto): String {
		if (chapterId == null) {
			// Create
			val chapter = Chapter()
			chapter.gameId = gameId
			chapter.color = chapterDto.color
			chapter.name = chapterDto.name
			questDao.insertChapter(chapter)
		} else {
			// Update
			val chapter = questDao.getChapter(chapterId) ?: throw ResourceNotFoundException("No chapter with chapterId=$chapterId")
			chapter.gameId = gameId
			chapter.color = chapterDto.color
			chapter.name = chapterDto.name
			questDao.updateChapter(chapter)
		}
		return "redirect:/admin/games/$gameId/quests"
	}
}
