package nl.lukasmiedema.locationquest.controller.dashboard

import nl.lukasmiedema.locationquest.controller.GamesController
import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.InventoryDto
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 */
@Controller
@RequestMapping("games/{game}/dashboard/history")
open class HistoryController {

	@Autowired private lateinit var questDao: QuestDao

	@GetMapping
	open fun getHistory(
			@ModelAttribute("team") team: TeamInfoDto,
			model: Model,
			@AuthenticationPrincipal player: Player): String {

		// Get the teams history
		val history = questDao.getClaimedQuests(team.teamId!!)

		model.addAttribute("history", history)
		model.addAttribute("activeTab", "HistoryTab")
		return "Dashboard"
	}

	@GetMapping("details/{questId}")
	open fun getDetails(
			@PathVariable("questId") questId: Int,
			@ModelAttribute("team") team: TeamInfoDto,
			model: Model): String {

		// Get this item from the teams history
		val quest = questDao.getClaimedQuest(questId, team.teamId!!) ?:
				throw ResourceNotFoundException("Either this quest does not exist or your team hasn't claimed it yet")

		// Also get the items for this
		val (yieldsInventory, requiresInventory) = questDao.getQuestItems(questId)

		// Put everything in the model
		model.addAttribute("item", quest)
		model.addAttribute("yieldsInventory", yieldsInventory)
		model.addAttribute("requiresInventory", requiresInventory)
		model.addAttribute("activeTab", "HistoryDetailsTab")
		return "Dashboard"
	}
}