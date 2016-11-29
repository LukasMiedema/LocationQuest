/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity.tables.records;


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

import nl.lukasmiedema.locationquest.entity.tables.Question;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
@Table(name = "QUESTION", schema = "LOCATION_GAME")
public class QuestionRecord extends UpdatableRecordImpl<QuestionRecord> implements Record7<Integer, Integer, String, String, Boolean, UUID, String> {

    private static final long serialVersionUID = 427166626;

    /**
     * Setter for <code>LOCATION_GAME.QUESTION.QUESTION_ID</code>.
     */
    public QuestionRecord setQuestionId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.QUESTION.QUESTION_ID</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID", unique = true, nullable = false, precision = 10)
    public Integer getQuestionId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>LOCATION_GAME.QUESTION.GAME_ID</code>.
     */
    public QuestionRecord setGameId(Integer value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.QUESTION.GAME_ID</code>.
     */
    @Column(name = "GAME_ID", nullable = false, precision = 10)
    @NotNull
    public Integer getGameId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>LOCATION_GAME.QUESTION.TITLE</code>.
     */
    public QuestionRecord setTitle(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.QUESTION.TITLE</code>.
     */
    @Column(name = "TITLE", nullable = false, length = 2147483647)
    @NotNull
    @Size(max = 2147483647)
    public String getTitle() {
        return (String) get(2);
    }

    /**
     * Setter for <code>LOCATION_GAME.QUESTION.TEXT</code>.
     */
    public QuestionRecord setText(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.QUESTION.TEXT</code>.
     */
    @Column(name = "TEXT", nullable = false, length = 2147483647)
    @NotNull
    @Size(max = 2147483647)
    public String getText() {
        return (String) get(3);
    }

    /**
     * Setter for <code>LOCATION_GAME.QUESTION.MAIN_TRACK</code>.
     */
    public QuestionRecord setMainTrack(Boolean value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.QUESTION.MAIN_TRACK</code>.
     */
    @Column(name = "MAIN_TRACK", nullable = false, precision = 1)
    public Boolean getMainTrack() {
        return (Boolean) get(4);
    }

    /**
     * Setter for <code>LOCATION_GAME.QUESTION.QR_CODE</code>.
     */
    public QuestionRecord setQrCode(UUID value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.QUESTION.QR_CODE</code>.
     */
    @Column(name = "QR_CODE", precision = 2147483647)
    public UUID getQrCode() {
        return (UUID) get(5);
    }

    /**
     * Setter for <code>LOCATION_GAME.QUESTION.PUZZLE_TEXT</code>.
     */
    public QuestionRecord setPuzzleText(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.QUESTION.PUZZLE_TEXT</code>.
     */
    @Column(name = "PUZZLE_TEXT", length = 2147483647)
    @Size(max = 2147483647)
    public String getPuzzleText() {
        return (String) get(6);
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
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Integer, String, String, Boolean, UUID, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, Integer, String, String, Boolean, UUID, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Question.QUESTION.QUESTION_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return Question.QUESTION.GAME_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Question.QUESTION.TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Question.QUESTION.TEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field5() {
        return Question.QUESTION.MAIN_TRACK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field6() {
        return Question.QUESTION.QR_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Question.QUESTION.PUZZLE_TEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getQuestionId();
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
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value5() {
        return getMainTrack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value6() {
        return getQrCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getPuzzleText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord value1(Integer value) {
        setQuestionId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord value2(Integer value) {
        setGameId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord value3(String value) {
        setTitle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord value4(String value) {
        setText(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord value5(Boolean value) {
        setMainTrack(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord value6(UUID value) {
        setQrCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord value7(String value) {
        setPuzzleText(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuestionRecord values(Integer value1, Integer value2, String value3, String value4, Boolean value5, UUID value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached QuestionRecord
     */
    public QuestionRecord() {
        super(Question.QUESTION);
    }

    /**
     * Create a detached, initialised QuestionRecord
     */
    public QuestionRecord(Integer questionId, Integer gameId, String title, String text, Boolean mainTrack, UUID qrCode, String puzzleText) {
        super(Question.QUESTION);

        set(0, questionId);
        set(1, gameId);
        set(2, title);
        set(3, text);
        set(4, mainTrack);
        set(5, qrCode);
        set(6, puzzleText);
    }
}
