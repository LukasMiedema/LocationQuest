package nl.lukasmiedema.locationquest.dao

import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.MultipleChoiceAnswer
import nl.lukasmiedema.locationquest.entity.tables.pojos.Question
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * @author Lukas Miedema
 */
@Repository
@Transactional
open class QuestDao {

	@Autowired private lateinit var sql: DSLContext

	/**
	 * Gets the next question not answered by this team. If the team
	 * has completed all the questions or the team does not exist, null
	 * is returned.
	 */
	open fun getNextQuestion(gameId: Int, teamId: Int): Question? {

		// Get the first unanswered question
		// This is fetched by doing an anti join on the answered questions
		// sorting by ID and then limiting to 1
		val q = Tables.QUESTION.`as`("q")
		val aq = Tables.ANSWERED_QUESTION.`as`("aq")
		return sql
				.select(*q.fields())
				.from(
						q.leftJoin(aq)
								.on(aq.QUESTION_ID.eq(q.QUESTION_ID))
								.and(aq.TEAM_ID.eq(teamId))
				)
				.where(aq.TEAM_ID.isNull)
				.and(q.GAME_ID.eq(gameId))
				.orderBy(q.QUESTION_ID.asc())
				.limit(1)
				.fetchOneInto(Question::class.java)
	}

	/**
	 * Returns a list of multiple choice answers for a question.
	 */
	open fun getQuestionOptions(questionId: Int): List<MultipleChoiceAnswer> = sql
			.selectFrom(Tables.MULTIPLE_CHOICE_ANSWER)
			.where(Tables.MULTIPLE_CHOICE_ANSWER.QUESTION_ID.eq(questionId))
			.fetchInto(MultipleChoiceAnswer::class.java)
}