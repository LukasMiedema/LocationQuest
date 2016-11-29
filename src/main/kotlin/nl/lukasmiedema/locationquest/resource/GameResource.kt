package nl.lukasmiedema.locationquest.resource

import nl.lukasmiedema.locationquest.LocationQuestApplication
import nl.lukasmiedema.locationquest.entity.Tables.*
import nl.lukasmiedema.locationquest.entity.tables.pojos.*
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import nl.lukasmiedema.locationquest.exception.UnauthorizedException
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.WebApplicationContext

/**
 * @author Lukas Miedema
 */
@RestController
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
@RequestMapping(LocationQuestApplication.REST_PREFIX + "games")
@Transactional
open class GameResource {

	@Autowired open lateinit var sql: DSLContext;
	@Autowired open var player: Player? = null;

	@RequestMapping("/{gameId}", method = arrayOf(RequestMethod.GET))
	open fun getGame(@PathVariable gameId: Int):Game? {
		val game = sql
				.selectFrom(GAME)
				.where(GAME.ID.eq(gameId))
				.fetchAnyInto(Game::class.java);
		if (game === null) throw ResourceNotFoundException("No such game: " + gameId);
		return game;
	}

	@RequestMapping("/", method = arrayOf(RequestMethod.GET))
	open fun getGames():List<Game> {
		return sql
				.selectFrom(GAME)
				.where(GAME.ALLOW_NEW_MEMBERS.eq(true)) // games that allow new members
				.orExists(DSL // or games that this player is already a member of
						.select()
						.from(TEAM.join(TEAM_PLAYER)
								.on(TEAM_PLAYER.TEAM_NAME.eq(TEAM.NAME)))
						.where(TEAM_PLAYER.PLAYER_SESSION_ID.eq(player?.sessionId))
				)
				.fetchInto(Game::class.java);
	}

}