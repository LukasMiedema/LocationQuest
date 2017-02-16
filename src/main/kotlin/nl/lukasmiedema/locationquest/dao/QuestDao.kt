package nl.lukasmiedema.locationquest.dao

import nl.lukasmiedema.locationquest.dto.ClaimedQuestInfoDto
import nl.lukasmiedema.locationquest.dto.InventoryDto
import nl.lukasmiedema.locationquest.dto.QuestCollectibleDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.Tables.*
import nl.lukasmiedema.locationquest.entity.tables.pojos.ClaimedQuest
import nl.lukasmiedema.locationquest.entity.tables.pojos.Collectible
import nl.lukasmiedema.locationquest.entity.tables.pojos.Quest
import nl.lukasmiedema.locationquest.entity.tables.pojos.QuestCollectible
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
		val quest = QUEST.`as`("quest")
		val claimed = CLAIMED_QUEST.`as`("claimed")
		val chapter = CHAPTER.`as`("chapter")

		return sql
				.select(*quest.fields())
				.from(
						quest.join(chapter)
								.on(quest.CHAPTER_ID.eq(chapter.CHAPTER_ID))
						.leftJoin(claimed)
								.on(claimed.QUEST_ID.eq(quest.QUEST_ID))
								.and(claimed.TEAM_ID.eq(teamId))
				)
				.where(claimed.TEAM_ID.isNull)
				.and(quest.REQUIRED)
				.and(chapter.GAME_ID.eq(gameId))
				.orderBy(quest.QUEST_ID.asc())
				.limit(1)
				.fetchOneInto(Quest::class.java)
	}

	/**
	 * Gets the quest by QR code.
	 */
	open fun getQuestByQR(code: UUID): Quest? = sql
			.selectFrom(QUEST)
			.where(QUEST.QR_CODE.eq(code))
			.fetchOneInto(Quest::class.java)

	/**
	 * Stores the claim into the database.
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
}