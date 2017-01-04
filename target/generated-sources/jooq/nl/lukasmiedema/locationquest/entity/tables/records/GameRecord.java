/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity.tables.records;


import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import nl.lukasmiedema.locationquest.entity.tables.Game;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
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
@Table(name = "GAME", schema = "LOCATION_GAME")
public class GameRecord extends UpdatableRecordImpl<GameRecord> implements Record6<Integer, String, UUID, Timestamp, Boolean, Boolean> {

    private static final long serialVersionUID = 311488465;

    /**
     * Setter for <code>LOCATION_GAME.GAME.ID</code>.
     */
    public GameRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.GAME.ID</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, precision = 10)
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>LOCATION_GAME.GAME.NAME</code>.
     */
    public GameRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.GAME.NAME</code>.
     */
    @Column(name = "NAME", nullable = false, length = 2147483647)
    @NotNull
    @Size(max = 2147483647)
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LOCATION_GAME.GAME.ADMIN_ID</code>.
     */
    public GameRecord setAdminId(UUID value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.GAME.ADMIN_ID</code>.
     */
    @Column(name = "ADMIN_ID", precision = 2147483647)
    public UUID getAdminId() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>LOCATION_GAME.GAME.TIMESTAMP</code>.
     */
    public GameRecord setTimestamp(Timestamp value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.GAME.TIMESTAMP</code>.
     */
    @Column(name = "TIMESTAMP", nullable = false, precision = 23, scale = 10)
    public Timestamp getTimestamp() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>LOCATION_GAME.GAME.ACTIVE</code>.
     */
    public GameRecord setActive(Boolean value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.GAME.ACTIVE</code>.
     */
    @Column(name = "ACTIVE", nullable = false, precision = 1)
    public Boolean getActive() {
        return (Boolean) get(4);
    }

    /**
     * Setter for <code>LOCATION_GAME.GAME.ALLOW_NEW_MEMBERS</code>.
     */
    public GameRecord setAllowNewMembers(Boolean value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.GAME.ALLOW_NEW_MEMBERS</code>.
     */
    @Column(name = "ALLOW_NEW_MEMBERS", nullable = false, precision = 1)
    public Boolean getAllowNewMembers() {
        return (Boolean) get(5);
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
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, UUID, Timestamp, Boolean, Boolean> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, UUID, Timestamp, Boolean, Boolean> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Game.GAME.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Game.GAME.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field3() {
        return Game.GAME.ADMIN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return Game.GAME.TIMESTAMP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field5() {
        return Game.GAME.ACTIVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field6() {
        return Game.GAME.ALLOW_NEW_MEMBERS;
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
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value3() {
        return getAdminId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getTimestamp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value5() {
        return getActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value6() {
        return getAllowNewMembers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameRecord value3(UUID value) {
        setAdminId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameRecord value4(Timestamp value) {
        setTimestamp(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameRecord value5(Boolean value) {
        setActive(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameRecord value6(Boolean value) {
        setAllowNewMembers(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameRecord values(Integer value1, String value2, UUID value3, Timestamp value4, Boolean value5, Boolean value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GameRecord
     */
    public GameRecord() {
        super(Game.GAME);
    }

    /**
     * Create a detached, initialised GameRecord
     */
    public GameRecord(Integer id, String name, UUID adminId, Timestamp timestamp, Boolean active, Boolean allowNewMembers) {
        super(Game.GAME);

        set(0, id);
        set(1, name);
        set(2, adminId);
        set(3, timestamp);
        set(4, active);
        set(5, allowNewMembers);
    }
}