package nl.lukasmiedema.locationquest.controller

import nl.lukasmiedema.locationquest.dao.QuestDao
import nl.lukasmiedema.locationquest.dto.GameTabDto
import nl.lukasmiedema.locationquest.dto.MessageDto
import nl.lukasmiedema.locationquest.dto.MultipleChoiceAnswerDto
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.dto.quest.*
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.*
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.exception.UnauthorizedException
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.util.*
import javax.validation.Valid

/**
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(GamesController.URL + "/{game}/dashboard")
open class DashboardController {

	companion object {
		private val TABS: Array<GameTabDto> = arrayOf(
				GameTabDto("QuestTab", ""),
				GameTabDto("HistoryTab", "history"),
				GameTabDto("TeamTab", "team")
		)
	}

	@Autowired private lateinit var sql: DSLContext
	@Autowired private lateinit var questDao: QuestDao

	@ModelAttribute("tabs")
	open fun tabs() = TABS

	@ModelAttribute("game")
	open fun getGame(@PathVariable("game") gameId: Int): Game {
		val game = sql
				.selectFrom(Tables.GAME)
				.where(Tables.GAME.ID.eq(gameId))
				.fetchOneInto(Game::class.java)
		if (game == null) {
			throw ResourceNotFoundException("No such game")
		} else {
			return game
		}
	}

	@ModelAttribute("team")
	open fun getTeamInfo(
			@PathVariable("game") gameId: Int,
			@AuthenticationPrincipal player: Player): TeamInfoDto {

		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")

		val teamInfo = sql
				.select(
						t.ID,
						t.NAME,
						t.COLOR,
						DSL.select(DSL.count())
								.from(Tables.TEAM_PLAYER)
								.where(Tables.TEAM_PLAYER.TEAM_ID.eq(t.ID))
								.asField<Any>("MEMBER_COUNT")
				)
				.from(t.join(tp).on(t.ID.eq(tp.TEAM_ID)))
				.where(t.GAME_ID.eq(gameId))
				.and(tp.PLAYER_SESSION_ID.eq(player.sessionId))
				.fetchOneInto(TeamInfoDto::class.java)

		if (teamInfo == null) {
			throw UnauthorizedException("Not enrolled")
		} else {
			return teamInfo
		}
	}

	@ModelAttribute("messages")
	open fun getMessages() = arrayListOf(MessageDto(MessageDto.MessageType.WARNING, "TESTING"))//ArrayList<MessageDto>()

	@GetMapping
	open fun getQuest(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			@ModelAttribute("messages") messages: MutableList<MessageDto>,
			@RequestParam("claim", required = false) claim: String?,
			model: Model): String {

		// Determine a quest dto impl
		val quest : QuestDto

		// Check game state
		if (!game.active) {
			quest = CompletedQuestDto(QuestPhase.CLOSED)
		} else {

			// Get next question
			val next = questDao.getNextQuestion(game.id!!, team.id!!)
			if (next == null) {

				// No more questions
				quest = CompletedQuestDto(QuestPhase.DONE)

			} else {

				// Check type
				when (next.type) {
					"MULTIPLE_CHOICE" -> quest =
							PuzzleQuestDto(next.title, next.text, questDao.getQuestionOptions(next.questionId))
					"QR_TEXT_FETCH" -> quest = FetchQuestDto(next.title, next.text)
					else -> throw IllegalStateException("Unknown quest type")
				}
			}
		}

		// Add claim message
		when (claim) {
			"error" -> messages.add(MessageDto(MessageDto.MessageType.WARNING, "Antwoord kon niet verwerkt worden"))
			"correct" -> messages.add(MessageDto(MessageDto.MessageType.SUCCESS, "Opdracht goedgekeurd!"))
			"wrong" -> messages.add(MessageDto(MessageDto.MessageType.ERROR, "Opdracht fout"))
		}

		model.addAttribute("quest", quest)
		model.addAttribute("activeTab", "QuestTab")
		return "Dashboard"
	}

	@PostMapping("/quest/mc")
	open fun postMultipleChoiceQuest(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			@ModelAttribute("messages") messages: MutableList<MessageDto>,
			model: Model,
			@Valid multipleChoiceAnswerDto: MultipleChoiceAnswerDto,
			bindingResult: BindingResult): String {

		// Check answer
		if (bindingResult.hasErrors()) {
			messages.add(MessageDto(MessageDto.MessageType.WARNING, "Vergeet niet een optie te selecteren"))
			return getQuest(game, team, messages, null, model)
		}

		// Determine redirect url
		val redirect = "redirect:/${GamesController.URL}/${game.id}/dashboard/?claim="

		// Validate
		val next = questDao.getNextQuestion(game.id!!, team.id!!)
		if (!game.active || next == null || next.type != "MULTIPLE_CHOICE") {
			return redirect + "error"
		}

		val answer = sql
				.selectFrom(Tables.MULTIPLE_CHOICE_ANSWER)
				.where(
						Tables.MULTIPLE_CHOICE_ANSWER.ANSWER_ID.eq(multipleChoiceAnswerDto.option))
				.fetchOneInto(MultipleChoiceAnswer::class.java)

		// Validate more
		if (answer == null || answer.questionId != next.questionId) {
			return redirect + "error"
		}

		// Save a multiple choice quest answer
		// It is stored regardless if it was correct
		val answeredQuest = sql.newRecord(Tables.ANSWERED_QUESTION)
		answeredQuest.questionId = next.questionId
		answeredQuest.answerId = answer.answerId
		answeredQuest.teamId = team.id
		answeredQuest.timestamp = Timestamp(System.currentTimeMillis())
		answeredQuest.store()

		// And redirect
		return redirect + (if (answer.correct) "correct" else "wrong")
	}

	@GetMapping("history")
	open fun getHistory(
			@ModelAttribute("game") game: Game,
			model: Model,
			@AuthenticationPrincipal player: Player): String {
		model.addAttribute("activeTab", "HistoryTab")
		return "Dashboard"
	}

	@GetMapping("team")
	open fun getTeam(
			@ModelAttribute("game") game: Game,
			@ModelAttribute("team") team: TeamInfoDto,
			model: Model,
			@AuthenticationPrincipal player: Player): String {

		val teamMembers = sql
				.select(*Tables.PLAYER.fields())
				.from(
						Tables.PLAYER.join(Tables.TEAM_PLAYER)
								.on(Tables.PLAYER.SESSION_ID.eq(Tables.TEAM_PLAYER.PLAYER_SESSION_ID)
								)
				).where(Tables.TEAM_PLAYER.TEAM_ID.eq(team.id))
				.fetchInto(Player::class.java)

		model.addAttribute("teamMembers", teamMembers)
		model.addAttribute("activeTab", "TeamTab")
		model.addAttribute("player", player)
		return "Dashboard"
	}
}