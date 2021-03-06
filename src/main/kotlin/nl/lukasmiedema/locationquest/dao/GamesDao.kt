package nl.lukasmiedema.locationquest.dao

import nl.lukasmiedema.locationquest.dto.GameInfoDto
import nl.lukasmiedema.locationquest.dto.TeamInfoDto
import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.Tables.*
import nl.lukasmiedema.locationquest.entity.tables.pojos.Game
import nl.lukasmiedema.locationquest.entity.tables.pojos.Player
import nl.lukasmiedema.locationquest.entity.tables.pojos.Team
import nl.lukasmiedema.locationquest.entity.tables.records.GameRecord
import nl.lukasmiedema.locationquest.exception.UnauthorizedException
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
class GamesDao {

	@Autowired private lateinit var sql: DSLContext


	/**
	 * Retrieve all games
	 */
	fun getGames(): List<Game> = sql.selectFrom(GAME).fetchInto(Game::class.java)

	/**
	 * Retrieve the game by the provided gameId. If the game does not exist, null is returned.
	 */
	fun getGame(gameId: Int): Game? = sql
			.selectFrom(GAME)
			.where(GAME.GAME_ID.eq(gameId))
			.fetchOneInto(Game::class.java)

	/**
	 * Update the game.
	 */
	fun updateGame(game: Game) {
		sql
				.update(GAME)
				.set(GAME.NAME, game.name)
				.set(GAME.ACTIVE, game.active)
				.set(GAME.ALLOW_NEW_MEMBERS, game.allowNewMembers)
				.where(GAME.GAME_ID.eq(game.gameId))
				.execute()
	}

	fun insertGame(game: Game): Game = sql
				.insertInto(GAME)
				.set(GAME.NAME, game.name)
				.set(GAME.ACTIVE, game.active)
				.set(GAME.ALLOW_NEW_MEMBERS, game.allowNewMembers)
				.returning(*GAME.fields())
				.fetchOne().into(Game::class.java)

	/**
	 * Retrieve detailed information about the teams for a given game.
	 * If the game does not exist an empty list is returned.
	 */
	fun getTeamsDetailed(gameId: Int): List<TeamInfoDto> {
		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")

		return sql
				.select(
						t.TEAM_ID,
						t.NAME,
						t.COLOR,
						DSL.select(DSL.count())
								.from(tp)
								.where(tp.TEAM_ID.eq(t.TEAM_ID))
								.asField<Any>("MEMBER_COUNT")
				)
				.from(t).where(t.GAME_ID.eq(gameId))
				.fetchInto(TeamInfoDto::class.java);
	}

	/**
	 * Get the team the given player is enrolled in + details. If the player is not enrolled, or either the player
	 * or the game does not exist, null is returned.
	 */
	fun getTeamDetailed(gameId: Int, playerId: Int): TeamInfoDto? {
		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")
		return sql
				.select(
						t.TEAM_ID,
						t.NAME,
						t.COLOR,
						DSL.select(DSL.count())
								.from(Tables.TEAM_PLAYER)
								.where(Tables.TEAM_PLAYER.TEAM_ID.eq(t.TEAM_ID))
								.asField<Any>("MEMBER_COUNT")
				)
				.from(t.join(tp).on(t.TEAM_ID.eq(tp.TEAM_ID)))
				.where(t.GAME_ID.eq(gameId))
				.and(tp.PLAYER_ID.eq(playerId))
				.fetchOneInto(TeamInfoDto::class.java)
	}

	/**
	 * Retrieve all games + info about the game in which the player is enrolled.
	 */
	fun getEnrolledGamesDetailed(playerId: Int): List<GameInfoDto> {
		val g = GAME.`as`("g")
		val t = Tables.TEAM.`as`("t")
		val tp = Tables.TEAM_PLAYER.`as`("tp")

		return sql
				.select(
						g.GAME_ID,
						g.NAME,
						g.TIMESTAMP,
						g.ACTIVE,
						g.ALLOW_NEW_MEMBERS,
						t.NAME.`as`("TEAM_NAME"),
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						tp.join(t) // find the team for every team player
								.on(t.TEAM_ID.eq(tp.TEAM_ID))
								.join(g) // find all games for every team player
								.on(t.GAME_ID.eq(g.GAME_ID))
				)
				.where(tp.PLAYER_ID.eq(playerId))
				.fetchInto(GameInfoDto::class.java)
	}

	/**
	 * Retrieve all games the player is enrolled in without extra information.
	 */
	fun getEnrolledGames(playerId: Int): List<Game> = sql
			.select(*GAME.fields())
			.from(GAME
					.join(Tables.TEAM).on(Tables.TEAM.GAME_ID.eq(GAME.GAME_ID))
					.join(Tables.TEAM_PLAYER).on(Tables.TEAM_PLAYER.TEAM_ID.eq(Tables.TEAM.TEAM_ID))
			)
			.where(Tables.TEAM_PLAYER.PLAYER_ID.eq(playerId))
			.fetchInto(Game::class.java)

	/**
	 * Returns all games with information about the relation with the player to the game, like team name,
	 * enrollment status.
	 */
	fun getOpenGamesDetailed(playerId: Int): List<GameInfoDto> {
		val g = GAME.`as`("g")
		val t = TEAM.`as`("t")
		val tp = TEAM_PLAYER.`as`("tp")

		return sql
				.select(
						g.GAME_ID,
						g.NAME,
						g.TIMESTAMP,
						g.ACTIVE,
						g.ALLOW_NEW_MEMBERS,
						t.NAME.`as`("TEAM_NAME"),
						t.COLOR.`as`("TEAM_COLOR")
				)
				.from(
						g.leftJoin(t.join(tp)
								.on(tp.TEAM_ID.eq(t.TEAM_ID)))
								.on(t.GAME_ID.eq(g.GAME_ID))
								.and(tp.PLAYER_ID.eq(playerId))
				)
				.where(
						tp.PLAYER_ID.isNotNull.or(g.ALLOW_NEW_MEMBERS)
				)
				.fetchInto(GameInfoDto::class.java)
	}

	/**
	 * Check if the player is already enrolled in the game. If either the game
	 * or the player does not exist, false is returned.
	 */
	fun isEnrolled(gameId: Int, playerId: Int): Boolean = sql.fetchExists(
			DSL.selectFrom(
					Tables.TEAM.join(Tables.TEAM_PLAYER)
							.on(Tables.TEAM.TEAM_ID.eq(Tables.TEAM_PLAYER.TEAM_ID))
			).where(Tables.TEAM_PLAYER.PLAYER_ID.eq(playerId))
					.and(Tables.TEAM.GAME_ID.eq(gameId)))

	/**
	 * Enroll the player into the provided team. If the player is already enrolled in the team,
	 * an exception is thrown.
	 */
	fun enroll(teamId: Int, playerId: Int) {
		val tp = Tables.TEAM_PLAYER
		sql.insertInto(tp, tp.PLAYER_ID, tp.TEAM_ID)
				.values(playerId, teamId).execute()
	}

	/**
	 * Create a new team and return that team.
	 */
	fun createTeam(gameId: Int, color: Int, name: String): Team = sql
			.insertInto(Tables.TEAM)
			.set(Tables.TEAM.GAME_ID, gameId)
			.set(Tables.TEAM.COLOR, color)
			.set(Tables.TEAM.NAME, name)
			.returning(*Tables.TEAM.fields())
			.fetchOne().into(Team::class.java)

	/**
	 * Returns a list of all players in the given team. If the team does not exist, an empty list is returned.
	 */
	fun getTeamMembers(teamId: Int): List<Player> = sql
			.select(*Tables.PLAYER.fields())
			.from(
					Tables.PLAYER.join(Tables.TEAM_PLAYER)
							.on(Tables.PLAYER.PLAYER_ID.eq(Tables.TEAM_PLAYER.PLAYER_ID)
							)
			).where(Tables.TEAM_PLAYER.TEAM_ID.eq(teamId))
			.fetchInto(Player::class.java)
}
