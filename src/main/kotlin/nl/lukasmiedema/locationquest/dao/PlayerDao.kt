package nl.lukasmiedema.locationquest.dao

import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.entity.tables.pojos.PlayerCredentials
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * @author Lukas Miedema
 */
@Transactional
@Repository
class PlayerDao {

	@Autowired private lateinit var sql: DSLContext

	/**
	 * Insert a new player into the database with the provided name and return
	 * the new player.
	 */
	fun createNewPlayer(name: String): Player {
		val p = Tables.PLAYER
		return sql
				.insertInto(p, p.NAME)
				.values(name)
				.returning(*p.fields())
				.fetchOne().into(Player::class.java)
	}

	/**
	 * Assign a new name to the player with the provided player id.
	 */
	fun renamePlayer(playerId: Int, name: String) {
		val p = Tables.PLAYER
		sql
				.update(p)
				.set(p.NAME, name)
				.where(p.PLAYER_ID.eq(playerId))
				.execute()
	}

	/**
	 * Retrieves the player credentials from the database.
	 */
	fun getCredentialsByEmail(email: String) : PlayerCredentials? = sql
			.selectFrom(Tables.PLAYER_CREDENTIALS)
			.where(Tables.PLAYER_CREDENTIALS.EMAIL_ADDRESS.eq(email))
			.fetchOneInto(PlayerCredentials::class.java)

	/**
	 * Retrieves the player credentials from the database.
	 */
	fun getCredentials(playerId: Int): PlayerCredentials? = sql
			.selectFrom(Tables.PLAYER_CREDENTIALS)
			.where(Tables.PLAYER_CREDENTIALS.PLAYER_ID.eq(playerId))
			.fetchOneInto(PlayerCredentials::class.java)

	/**
	 * Returns true if the player has credentials. False otherwise.
	 */
	fun hasCredentials(playerId: Int): Boolean = sql.select(DSL.field(DSL.exists(
			DSL.selectFrom(Tables.PLAYER_CREDENTIALS)
					.where(Tables.PLAYER_CREDENTIALS.PLAYER_ID.eq(playerId))
	))).fetchAny(0) as Boolean

	fun setPlayerCredentials(playerCredentials: PlayerCredentials) {
		val record = sql.newRecord(Tables.PLAYER_CREDENTIALS, playerCredentials)
		if (hasCredentials(playerCredentials.playerId))
			record.update()
		else
			record.insert()
	}

	/**
	 * Retrieve the player by player id, or null if the player does not exist.
	 */
	fun getPlayer(playerId: Int): Player? = sql
			.selectFrom(Tables.PLAYER)
			.where(Tables.PLAYER.PLAYER_ID.eq(playerId))
			.fetchOneInto(Player::class.java)
}
