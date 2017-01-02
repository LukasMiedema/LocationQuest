/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity.tables.records;


import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import nl.lukasmiedema.locationquest.entity.tables.TeamPlayer;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "TEAM_PLAYER", schema = "LOCATION_GAME", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PLAYER_SESSION_ID", "GAME_ID", "TEAM_NAME"})
})
public class TeamPlayerRecord extends UpdatableRecordImpl<TeamPlayerRecord> implements Record3<UUID, Integer, String> {

    private static final long serialVersionUID = 1789931209;

    /**
     * Setter for <code>LOCATION_GAME.TEAM_PLAYER.PLAYER_SESSION_ID</code>.
     */
    public TeamPlayerRecord setPlayerSessionId(UUID value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.TEAM_PLAYER.PLAYER_SESSION_ID</code>.
     */
    @Column(name = "PLAYER_SESSION_ID", nullable = false, precision = 2147483647)
    @NotNull
    public UUID getPlayerSessionId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>LOCATION_GAME.TEAM_PLAYER.GAME_ID</code>.
     */
    public TeamPlayerRecord setGameId(Integer value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.TEAM_PLAYER.GAME_ID</code>.
     */
    @Column(name = "GAME_ID", nullable = false, precision = 10)
    @NotNull
    public Integer getGameId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>LOCATION_GAME.TEAM_PLAYER.TEAM_NAME</code>.
     */
    public TeamPlayerRecord setTeamName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.TEAM_PLAYER.TEAM_NAME</code>.
     */
    @Column(name = "TEAM_NAME", nullable = false, length = 2147483647)
    @NotNull
    @Size(max = 2147483647)
    public String getTeamName() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record3<UUID, Integer, String> key() {
        return (Record3) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<UUID, Integer, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<UUID, Integer, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return TeamPlayer.TEAM_PLAYER.PLAYER_SESSION_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return TeamPlayer.TEAM_PLAYER.GAME_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return TeamPlayer.TEAM_PLAYER.TEAM_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value1() {
        return getPlayerSessionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getGameId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getTeamName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamPlayerRecord value1(UUID value) {
        setPlayerSessionId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamPlayerRecord value2(Integer value) {
        setGameId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamPlayerRecord value3(String value) {
        setTeamName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamPlayerRecord values(UUID value1, Integer value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TeamPlayerRecord
     */
    public TeamPlayerRecord() {
        super(TeamPlayer.TEAM_PLAYER);
    }

    /**
     * Create a detached, initialised TeamPlayerRecord
     */
    public TeamPlayerRecord(UUID playerSessionId, Integer gameId, String teamName) {
        super(TeamPlayer.TEAM_PLAYER);

        set(0, playerSessionId);
        set(1, gameId);
        set(2, teamName);
    }
}
