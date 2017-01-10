package nl.lukasmiedema.locationquest.controller

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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Lukas Miedema
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(GamesController.URL + "/{game}/dashboard")
open class DashboardController {

	@Autowired
	private lateinit var sql: DSLContext

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

	@GetMapping
	open fun getQuest(
			game: Game,
			team: TeamInfoDto,
			model: Model,
			@AuthenticationPrincipal player: Player): String {

		// Determine a quest dto impl
		val quest : QuestDto?

		// Check game state
		if (!game.active) {

			quest = CompletedQuestDto("Spel gesloten", QuestPhase.CLOSED)

		} else {

			// Get next question
			val q = Tables.QUESTION.`as`("q")
			val aq = Tables.ANSWERED_QUESTION.`as`("aq")
			val nextQuestion: Question? = sql
					.select(*q.fields())
					.from(
							q.leftJoin(aq)
									.on(aq.QUESTION_ID.eq(q.QUESTION_ID))
									.and(aq.TEAM_ID.eq(team.id))
									.and(aq.COMPLETE.notEqual(true))
					).orderBy(q.QUESTION_ID.asc())
					.limit(1)
					.fetchOneInto(Question::class.java)

			if (nextQuestion == null) {

				// No more questions
				quest = CompletedQuestDto("Geen vragen meer", QuestPhase.DONE)

			} else {

				// Check if it's already partially answered
				val nextAnswer: AnsweredQuestion? = sql
						.selectFrom(Tables.ANSWERED_QUESTION)
						.where(
								Tables.ANSWERED_QUESTION.QUESTION_ID.eq(nextQuestion.questionId)
								.and(Tables.ANSWERED_QUESTION.TEAM_ID.eq(team.id))
						)
						.fetchOneInto(AnsweredQuestion::class.java)

				if (nextAnswer == null && nextQuestion.puzzleText != null) {

					// Not partially answered --> not answered at all
					// Show the puzzle question with all the options
					val options = sql
							.selectFrom(Tables.MULTIPLE_CHOICE_ANSWER)
							.where(Tables.MULTIPLE_CHOICE_ANSWER.QUESTION_ID.eq(nextQuestion.questionId))
							.fetchInto(MultipleChoiceAnswer::class.java)

					quest = PuzzleQuestDto(nextQuestion.puzzleTitle, nextQuestion.puzzleText, options)

				} else {

					// It's quest-o'-clock, show the quest
					quest = FetchQuestDto(nextQuestion.title, nextQuestion.text)
				}
			}
		}

		model.addAttribute("quest", quest)
		model.addAttribute("tab", "QuestTab")
		return "Dashboard"
	}

	@GetMapping("history")
	open fun getHistory(
			game: Game,
			model: Model,
			@AuthenticationPrincipal player: Player): String {
		model.addAttribute("tab", "HistoryTab")
		return "Dashboard"
	}

	@GetMapping("team")
	open fun getTeam(
			game: Game,
			model: Model,
			@AuthenticationPrincipal player: Player): String {
		model.addAttribute("tab", "TeamTab")
		return "Dashboard"
	}
}