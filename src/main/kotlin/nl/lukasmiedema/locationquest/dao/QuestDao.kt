package nl.lukasmiedema.locationquest.dao

import nl.lukasmiedema.locationquest.dto.ChapterDto
import nl.lukasmiedema.locationquest.dto.ClaimedQuestInfoDto
import nl.lukasmiedema.locationquest.dto.InventoryDto
import nl.lukasmiedema.locationquest.dto.QuestCollectibleDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.Tables.*
import nl.lukasmiedema.locationquest.entity.tables.pojos.*
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record4
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Lukas Miedema
 */
@Repository
@Transactional
open class QuestDao {

	@Autowired private lateinit var sql: DSLContext

	/**
	 * Gets the next required question not answered by this team. If the team
	 * has completed all the questions or the team does not exist, null
	 * is returned.
	 */
	open fun getNextQuest(gameId: Int, teamId: Int): Quest? {

		// Get the first unanswered question
		// This is fetched by doing an anti join on the answered questions
		// sorting by ID and then limiting to 1
		return sql
				.select(*QUEST.fields())
				.from(
						QUEST.join(CHAPTER)
								.on(QUEST.CHAPTER_ID.eq(CHAPTER.CHAPTER_ID))
						.leftJoin(CLAIMED_QUEST)
								.on(CLAIMED_QUEST.QUEST_ID.eq(QUEST.QUEST_ID))
								.and(CLAIMED_QUEST.TEAM_ID.eq(teamId))
				)
				.where(CLAIMED_QUEST.TEAM_ID.isNull)
				.and(QUEST.REQUIRED)
				.and(CHAPTER.GAME_ID.eq(gameId))
				.orderBy(QUEST.QUEST_ID.asc())
				.limit(1)
				.fetchOneInto(Quest::class.java)
	}

	/**
	 * Gets the quest by QR code.
	 */
	open fun getQuestByQR(gameId: Int, code: UUID): Quest? = sql
			.select(*QUEST.fields())
			.from(QUEST
					.join(CHAPTER).on(QUEST.CHAPTER_ID.eq(CHAPTER.CHAPTER_ID))
					.join(GAME).on(GAME.GAME_ID.eq(CHAPTER.GAME_ID))
			)
			.where(QUEST.QR_CODE.eq(code))
			.and(GAME.GAME_ID.eq(gameId))
			.fetchOneInto(Quest::class.java)

	/**
	 * Stores the claim into the database.
	 * This method will always perform an INSERT and as such it will throw an Exception if the
	 * record already exists.
	 */
	open fun insertClaim(claim: ClaimedQuest): Unit {
		sql.newRecord(CLAIMED_QUEST, claim).store()
	}

	/**
	 * Returns the items of a team.
	 */
	open fun getInventory(teamId: Int): InventoryDto {
		val data : Array<out Record4<Int, String, UUID, Int>> = sql
				.select(
						COLLECTIBLE.COLLECTIBLE_ID,
						COLLECTIBLE.NAME,
						COLLECTIBLE.FILE_ID,
						DSL.sum(QUEST_COLLECTIBLE.YIELDS).minus(DSL.sum(QUEST_COLLECTIBLE.REQUIRES)).cast(Int::class.javaPrimitiveType)
				)
				.from(
						CLAIMED_QUEST
								.join(QUEST_COLLECTIBLE).on(CLAIMED_QUEST.QUEST_ID.eq(QUEST_COLLECTIBLE.QUEST_ID))
								.join(COLLECTIBLE).on(COLLECTIBLE.COLLECTIBLE_ID.eq(QUEST_COLLECTIBLE.COLLECTIBLE_ID))
				)
				.where(CLAIMED_QUEST.TEAM_ID.eq(teamId))
				.groupBy(*COLLECTIBLE.fields())
				.fetchArray()

		return InventoryDto(data.map { InventoryDto.InventoryItem(it.into(Collectible::class.java), it.value4())} )
	}

	/**
	 * Gets the items needed to claim this quest.
	 */
	open fun getQuestCollectibles(questId: Int): List<QuestCollectibleDto> = sql
			.select(*COLLECTIBLE.fields(), *QUEST_COLLECTIBLE.fields())
			.from(
					QUEST_COLLECTIBLE
							.join(COLLECTIBLE).on(COLLECTIBLE.COLLECTIBLE_ID.eq(QUEST_COLLECTIBLE.COLLECTIBLE_ID))
			)
			.where(QUEST_COLLECTIBLE.QUEST_ID.eq(questId))
			.fetchArray()
			.map { QuestCollectibleDto(it.into(QuestCollectible::class.java), it.into(Collectible::class.java)) }

	/**
	 * Returns all claimed quests by the team, including the quest definitions.
	 */
	open fun getClaimedQuests(teamId: Int): List<ClaimedQuestInfoDto> = sql
			.select(*CLAIMED_QUEST.fields(), *QUEST.fields())
			.from(CLAIMED_QUEST.join(QUEST).on(CLAIMED_QUEST.QUEST_ID.eq(QUEST.QUEST_ID)))
			.where(CLAIMED_QUEST.TEAM_ID.eq(teamId))
			.fetchArray()
			.map {
				val quest = it.into(Quest::class.java)
				val claimedQuest = it.into(ClaimedQuest::class.java)
				ClaimedQuestInfoDto(claimedQuest, quest, getQuestCollectibles(quest.questId))
			}

	/**
	 * Checks if a team has claimed the given quest.
	 */
	open fun isClaimed(questId: Int, teamId: Int): Boolean {
		val result: Int = sql
				.selectCount()
				.from(CLAIMED_QUEST)
				.where(CLAIMED_QUEST.QUEST_ID.eq(questId))
				.and(CLAIMED_QUEST.TEAM_ID.eq(teamId))
				.fetchOne(DSL.count())
		return result > 0
	}

	/**
	 * Gets the chapter for a given chapterId.
	 */
	open fun getChapter(chapterId: Int): Chapter? = sql
			.selectFrom(CHAPTER)
			.where(CHAPTER.CHAPTER_ID.eq(chapterId))
			.fetchOneInto(Chapter::class.java)

	/**
	 * Fetches all chapters for a given gameId, with info about if the team already has the chapter.
	 */
	open fun getChaptersByGame(gameId: Int, teamId: Int): List<ChapterDto> = sql
			.select(
					*CHAPTER.fields(),

					// Select number of claimed quests for this chapter
					DSL.field(
						DSL.exists(
								DSL.selectFrom(CLAIMED_QUEST)
										.where(CLAIMED_QUEST.QUEST_ID.eq(QUEST.QUEST_ID))
										.and(CLAIMED_QUEST.TEAM_ID.eq(teamId))
						)
					).`as`("CLAIMED")
			)
			.from(CHAPTER.join(QUEST).on(QUEST.CHAPTER_ID.eq(CHAPTER.CHAPTER_ID)))
			.where(CHAPTER.GAME_ID.eq(gameId))
			.and(QUEST.REQUIRED)
			.fetchInto(ChapterDto::class.java)
}