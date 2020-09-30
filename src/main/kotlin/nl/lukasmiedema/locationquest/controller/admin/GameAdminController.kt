package nl.lukasmiedema.locationquest.controller.admin

import nl.lukasmiedema.locationquest.dao.GamesDao
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.GameDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.service.I18nService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author Lukas Miedema
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("admin/games")
class GameAdminController {

	@Autowired
	private lateinit var gamesDao: GamesDao

	@Autowired
	private lateinit var questDao: QuestDao

	@Autowired
	private lateinit var i18n: I18nService

	fun view(gameId: Int?, model: Model, dto: GameDto?): String {
		if (gameId == null) {
			model.addAttribute("game", null)
			model.addAttribute("gameDto", dto ?: GameDto().apply {
				name = i18n["admin.game.createNew"]
				allowNewMembers = false
				active = true
			})
		} else {
			val game = gamesDao.getGame(gameId) ?: throw ResourceNotFoundException("No game with the id ${gameId}")
			model.addAttribute("game", game)

			val teams = gamesDao.getTeamsDetailed(game.gameId)
			model.addAttribute("teams", teams)

			val allQuests = questDao.getQuestsByGame(game.gameId)
			val requiredQuests = allQuests.filter { it.required }
			val optionalQuests = allQuests.filterNot { it.required }
			model.addAttribute("quests", requiredQuests + optionalQuests)

			val claimedQuests = teams
					.map { it.teamId!! to questDao.getClaimedQuests(it.teamId!!).map { it.quest.questId!! to it }.toMap() }
					.toMap()
			model.addAttribute("claimedQuests", claimedQuests)

			if (dto == null)
				model.addAttribute("gameDto", GameDto().apply {
					name = game.name
					allowNewMembers = game.allowNewMembers
					active = game.active
				})
			else
				model.addAttribute("gameDto", dto)
		}
		return "admin/GameAdmin";
	}

	@GetMapping("/{game}")
	fun getEdit(@PathVariable("game") gameId: Int, model: Model): String {
		return view(gameId, model, null)
	}

	@GetMapping("/new")
	fun getCreate(model: Model): String {
		return view(null, model, null)
	}

	@PostMapping("/{game}")
	fun postEdit(@PathVariable("game") gameId: Int?,
			 @Valid @ModelAttribute dto: GameDto,
			 binding: BindingResult, model: Model): String {
		if (binding.hasErrors())
			return view(gameId, model, dto)

		// Save the form
		val newGameId: Int
		if (gameId == null) {
			val game = Game()
			game.name = dto.name
			game.active = dto.active
			game.allowNewMembers = dto.allowNewMembers
			newGameId = gamesDao.insertGame(game).gameId
		} else {
			val game = gamesDao.getGame(gameId) ?: throw ResourceNotFoundException("No game with the id ${gameId}")
			game.name = dto.name
			game.active = dto.active
			game.allowNewMembers = dto.allowNewMembers
			gamesDao.updateGame(game)
			newGameId = game.gameId
		}

		return "redirect:/admin/games/$newGameId"
	}

	@PostMapping("/new")
	fun postCreate(@Valid @ModelAttribute dto: GameDto,
			 binding: BindingResult, model: Model): String {
		return postEdit(null, dto, binding, model)
	}
}
