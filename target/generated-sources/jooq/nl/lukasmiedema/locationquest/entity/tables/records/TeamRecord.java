/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity.tables.records;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import nl.lukasmiedema.locationquest.entity.tables.Team;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
@Table(name = "TEAM", schema = "LOCATION_GAME", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"GAME_ID", "NAME"})
})
public class TeamRecord extends UpdatableRecordImpl<TeamRecord> implements Record4<Integer, Integer, String, Integer> {

    private static final long serialVersionUID = 583478989;

    /**
     * Setter for <code>LOCATION_GAME.TEAM.ID</code>.
     */
    public TeamRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.TEAM.ID</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, precision = 10)
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>LOCATION_GAME.TEAM.GAME_ID</code>.
     */
    public TeamRecord setGameId(Integer value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.TEAM.GAME_ID</code>.
     */
    @Column(name = "GAME_ID", nullable = false, precision = 10)
    @NotNull
    public Integer getGameId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>LOCATION_GAME.TEAM.NAME</code>.
     */
    public TeamRecord setName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.TEAM.NAME</code>.
     */
    @Column(name = "NAME", nullable = false, length = 2147483647)
    @NotNull
    @Size(max = 2147483647)
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>LOCATION_GAME.TEAM.COLOR</code>.
     */
    public TeamRecord setColor(Integer value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.TEAM.COLOR</code>.
     */
    @Column(name = "COLOR", nullable = false, precision = 10)
    @NotNull
    public Integer getColor() {
        return (Integer) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, Integer, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, Integer, String, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Team.TEAM.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return Team.TEAM.GAME_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Team.TEAM.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return Team.TEAM.COLOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
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
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamRecord value2(Integer value) {
        setGameId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamRecord value4(Integer value) {
        setColor(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamRecord values(Integer value1, Integer value2, String value3, Integer value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TeamRecord
     */
    public TeamRecord() {
        super(Team.TEAM);
    }

    /**
     * Create a detached, initialised TeamRecord
     */
    public TeamRecord(Integer id, Integer gameId, String name, Integer color) {
        super(Team.TEAM);

        set(0, id);
        set(1, gameId);
        set(2, name);
        set(3, color);
    }
}