/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import nl.lukasmiedema.locationquest.entity.Keys;
import nl.lukasmiedema.locationquest.entity.LocationGame;
import nl.lukasmiedema.locationquest.entity.tables.records.GameRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class Game extends TableImpl<GameRecord> {

    private static final long serialVersionUID = 2023608481;

    /**
     * The reference instance of <code>LOCATION_GAME.GAME</code>
     */
    public static final Game GAME = new Game();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GameRecord> getRecordType() {
        return GameRecord.class;
    }

    /**
     * The column <code>LOCATION_GAME.GAME.ID</code>.
     */
    public final TableField<GameRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("(NEXT VALUE FOR LOCATION_GAME.SYSTEM_SEQUENCE_8A74585A_896B_4897_8678_EA26D538291D)", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>LOCATION_GAME.GAME.NAME</code>.
     */
    public final TableField<GameRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>LOCATION_GAME.GAME.ADMIN_ID</code>.
     */
    public final TableField<GameRecord, UUID> ADMIN_ID = createField("ADMIN_ID", org.jooq.impl.SQLDataType.UUID, this, "");

    /**
     * The column <code>LOCATION_GAME.GAME.TIMESTAMP</code>.
     */
    public final TableField<GameRecord, Timestamp> TIMESTAMP = createField("TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.field("NOW()", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>LOCATION_GAME.GAME.ACTIVE</code>.
     */
    public final TableField<GameRecord, Boolean> ACTIVE = createField("ACTIVE", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("TRUE", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>LOCATION_GAME.GAME.ALLOW_NEW_MEMBERS</code>.
     */
    public final TableField<GameRecord, Boolean> ALLOW_NEW_MEMBERS = createField("ALLOW_NEW_MEMBERS", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("TRUE", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>LOCATION_GAME.GAME</code> table reference
     */
    public Game() {
        this("GAME", null);
    }

    /**
     * Create an aliased <code>LOCATION_GAME.GAME</code> table reference
     */
    public Game(String alias) {
        this(alias, GAME);
    }

    private Game(String alias, Table<GameRecord> aliased) {
        this(alias, aliased, null);
    }

    private Game(String alias, Table<GameRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return LocationGame.LOCATION_GAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<GameRecord, Integer> getIdentity() {
        return Keys.IDENTITY_GAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<GameRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<GameRecord>> getKeys() {
        return Arrays.<UniqueKey<GameRecord>>asList(Keys.CONSTRAINT_2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<GameRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<GameRecord, ?>>asList(Keys.CONSTRAINT_21);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game as(String alias) {
        return new Game(alias, this);
    }

    /**
     * Rename this table
     */
    public Game rename(String name) {
        return new Game(name, null);
    }
}
