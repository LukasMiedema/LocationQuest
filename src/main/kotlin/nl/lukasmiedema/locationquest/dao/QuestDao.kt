package nl.lukasmiedema.locationquest.dao

import nl.lukasmiedema.locationquest.dto.*
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
class QuestDao {

	@Autowired private lateinit var sql: DSLContext

	/**
	 * Gets all quests in a particular game.
	 */
	fun getQuestsByGame(gameId: Int): List<QuestDto> {
		val quests = sql
				.select(*QUEST.fields())
				.from(QUEST)
				.join(CHAPTER).on(QUEST.CHAPTER_ID.eq(CHAPTER.CHAPTER_ID))
				.where(CHAPTER.GAME_ID.eq(gameId))
				.orderBy(QUEST.QUEST_ID.asc())
				.fetchInto(QuestDto::class.java)
		quests.forEach { it.answers = this.getQuestAnswers(it.questId) }
		return quests
	}

	/**
	 * Gets the next required question not answered by this team. If the team
	 * has completed all the questions or the team does not exist, null
	 * is returned.
	 */
	fun getNextQuest(gameId: Int, teamId: Int): Quest? {

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
	 * Fetch one quest, or null.
	 */
	fun getQuest(questId: Int): Quest? = sql
			.selectFrom(QUEST)
			.where(QUEST.QUEST_ID.eq(questId))
			.fetchOneInto(Quest::class.java)

	/**
	 * Update an existing quest.
	 */
	fun updateQuest(quest: Quest): Quest = sql
			.update(QUEST)
			.set(QUEST.CHAPTER_ID, quest.chapterId)
			.set(QUEST.NAME, quest.name)
			.set(QUEST.FETCH_TEXT, quest.fetchText)
			.set(QUEST.SCAN_TEXT, quest.scanText)
			.set(QUEST.REQUIRED, quest.required)
			.set(QUEST.QR_CODE, quest.qrCode)
			.set(QUEST.PASSCODE_TEXT, quest.passcodeText)
			.where(QUEST.QUEST_ID.eq(quest.questId))
			.returning(*QUEST.fields())
			.fetchOne().into(Quest::class.java)

	/**
	 * Create a new quest.
	 */
	fun insertQuest(quest: Quest): Quest = sql
			.insertInto(QUEST)
			.set(QUEST.CHAPTER_ID, quest.chapterId)
			.set(QUEST.NAME, quest.name)
			.set(QUEST.FETCH_TEXT, quest.fetchText)
			.set(QUEST.SCAN_TEXT, quest.scanText)
			.set(QUEST.REQUIRED, quest.required)
			.set(QUEST.QR_CODE, quest.qrCode)
			.set(QUEST.PASSCODE_TEXT, quest.passcodeText)
			.returning(*QUEST.fields())
			.fetchOne().into(Quest::class.java)

	/**
	 * Gets the quest by QR code. Quests may have the same QR code in certain circumstances
	 */
	fun getQuestByQR(gameId: Int, code: String): List<QuestDto> {
		return sql
				.select(*QUEST.fields(), GAME.GAME_ID)
				.from(QUEST
						.join(CHAPTER).on(QUEST.CHAPTER_ID.eq(CHAPTER.CHAPTER_ID))
						.join(GAME).on(GAME.GAME_ID.eq(CHAPTER.GAME_ID))
				)
				.where(QUEST.QR_CODE.eq(code))
				.and(GAME.GAME_ID.eq(gameId))
				.orderBy(QUEST.QUEST_ID.asc())
				.fetch { result -> result.into(QuestDto::class.java)
						.also { it.answers = getQuestAnswers(it.questId) }}
	}

	/**
	 * Stores the claim into the database.
	 * This method will always perform an INSERT and as such it will throw an Exception if the
	 * record already exists.
	 */
	fun insertClaim(claim: ClaimedQuest): Unit {
		sql.newRecord(CLAIMED_QUEST, claim).store()
	}

	/**
	 * Returns the items of a team.
	 */
	fun getInventory(teamId: Int): InventoryDto {
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
	 * Returns a pair of the yields inventory and requires inventory for the quest.
	 */
	fun getQuestItems(questId: Int): QuestInventoryDto {
		// Get all collectible data
		val items = sql
				.select(*COLLECTIBLE.fields(), *QUEST_COLLECTIBLE.fields())
				.from(
						QUEST_COLLECTIBLE
								.join(COLLECTIBLE).on(COLLECTIBLE.COLLECTIBLE_ID.eq(QUEST_COLLECTIBLE.COLLECTIBLE_ID))
				)
				.where(QUEST_COLLECTIBLE.QUEST_ID.eq(questId))
				.fetchArray()
				.map { QuestCollectibleDto(it.into(QuestCollectible::class.java), it.into(Collectible::class.java)) }

		// Convert
		val yieldsInventory = InventoryDto(
				items.map({ InventoryDto.InventoryItem(it.collectible, it.questCollectible.yields) })
						.filter { it.count != 0 })

		val requiresInventory = InventoryDto(
				items.map({ InventoryDto.InventoryItem(it.collectible, it.questCollectible.requires) })
						.filter { it.count != 0 })

		return QuestInventoryDto(yieldsInventory, requiresInventory)
	}

	/**
	 * Returns all claimed quests by the team, including the quest definitions.
	 */
	fun getClaimedQuests(teamId: Int): List<ClaimedQuestInfoDto> = sql
			.select(*CLAIMED_QUEST.fields(), *QUEST.fields(), *CHAPTER.fields())
			.from(CLAIMED_QUEST
					.join(QUEST).on(CLAIMED_QUEST.QUEST_ID.eq(QUEST.QUEST_ID))
					.join(CHAPTER).on(CHAPTER.CHAPTER_ID.eq(QUEST.CHAPTER_ID)))
			.where(CLAIMED_QUEST.TEAM_ID.eq(teamId))
			.orderBy(CLAIMED_QUEST.CLAIMED_AT.desc())
			.fetchArray()
			.map {
				val quest = it.into(*QUEST.fields()).into(Quest::class.java)
				val claimedQuest = it.into(*CLAIMED_QUEST.fields()).into(ClaimedQuest::class.java)
				val chapter = it.into(*CHAPTER.fields()).into(ChapterDto::class.java)
				ClaimedQuestInfoDto(chapter, claimedQuest, quest)
			}

	/**
	 * Returns a single claimed quest for a given quest and team id. If the team has not yet claimed the quest,
	 * null will be returned.
	 */
	fun getClaimedQuest(questId: Int, teamId: Int): ClaimedQuestInfoDto? = sql
			.select(*CLAIMED_QUEST.fields(), *QUEST.fields(), *CHAPTER.fields())
			.from(CLAIMED_QUEST
					.join(QUEST).on(CLAIMED_QUEST.QUEST_ID.eq(QUEST.QUEST_ID))
					.join(CHAPTER).on(CHAPTER.CHAPTER_ID.eq(QUEST.CHAPTER_ID)))
			.where(CLAIMED_QUEST.QUEST_ID.eq(questId))
			.and(CLAIMED_QUEST.TEAM_ID.eq(teamId))
			.fetchOne {
				val quest = it.into(*QUEST.fields()).into(Quest::class.java)
				val claimedQuest = it.into(*CLAIMED_QUEST.fields()).into(ClaimedQuest::class.java)
				val chapter = it.into(*CHAPTER.fields()).into(ChapterDto::class.java)
				ClaimedQuestInfoDto(chapter, claimedQuest, quest)
			}

	/**
	 * Checks if a team has claimed the given quest.
	 */
	fun isClaimed(questId: Int, teamId: Int): Boolean {
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
	fun getChapter(chapterId: Int): Chapter? = sql
			.selectFrom(CHAPTER)
			.where(CHAPTER.CHAPTER_ID.eq(chapterId))
			.fetchOneInto(Chapter::class.java)

	/**
	 * Update the chapter
	 */
	fun updateChapter(chapter: Chapter) {
		sql
				.update(CHAPTER)
				.set(CHAPTER.NAME, chapter.name)
				.set(CHAPTER.COLOR, chapter.color)
				.where(CHAPTER.CHAPTER_ID.eq(chapter.chapterId))
				.execute()
	}

	/**
	 * Create a new chapter.
	 */
	fun insertChapter(chapter: Chapter) {
		sql
				.insertInto(CHAPTER)
				.set(CHAPTER.GAME_ID, chapter.gameId)
				.set(CHAPTER.NAME, chapter.name)
				.set(CHAPTER.COLOR, chapter.color)
				.execute()
	}

	/**
	 * Get all chapers for the given game.
	 */
	fun getChaptersByGame(gameId: Int): List<ChapterDto> = sql
			.selectFrom(CHAPTER)
			.where(CHAPTER.GAME_ID.eq(gameId))
			.fetchInto(ChapterDto::class.java)

	/**
	 * Fetches all quests with chapter info for a given gameId, with info about if the team already has the chapter.
	 * This returns one entry for every quest in the system, with chapter info for the quest it belongs to and a
	 * boolean indicating if the chapter has been claimed.
	 */
	fun getQuestChapterByGameAndTeam(gameId: Int, teamId: Int): List<ChapterDto> {
		val record = sql
				.select(
						*CHAPTER.fields(),

						// Select number of claimed quests for this chapter
						DSL.field(
								DSL.exists(
										DSL.selectFrom(CLAIMED_QUEST)
												.where(CLAIMED_QUEST.QUEST_ID.eq(QUEST.QUEST_ID))
												.and(CLAIMED_QUEST.TEAM_ID.eq(teamId))
								)
						).`as`("claimed")
				)
				.from(CHAPTER.join(QUEST).on(QUEST.CHAPTER_ID.eq(CHAPTER.CHAPTER_ID)))
				.where(CHAPTER.GAME_ID.eq(gameId))
				.and(QUEST.REQUIRED)
				.orderBy(CHAPTER.CHAPTER_ID.asc(), QUEST.QUEST_ID.asc())
				.fetch()

		return record.map { it.into(ChapterDto::class.java) }
	}

	/**
	 * Get all answers for a quest.
	 */
	fun getQuestAnswers(questId: Int): List<QuestAnswer> = sql
			.selectFrom(QUEST_ANSWER)
			.where(QUEST_ANSWER.QUEST_ID.eq(questId))
			.fetchInto(QuestAnswer::class.java)
}
