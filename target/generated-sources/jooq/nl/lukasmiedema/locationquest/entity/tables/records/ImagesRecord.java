/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity.tables.records;


import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import nl.lukasmiedema.locationquest.entity.tables.Images;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


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
@Table(name = "IMAGES", schema = "LOCATION_GAME")
public class ImagesRecord extends TableRecordImpl<ImagesRecord> implements Record2<UUID, byte[]> {

    private static final long serialVersionUID = 230108711;

    /**
     * Setter for <code>LOCATION_GAME.IMAGES.IMAGE_ID</code>.
     */
    public ImagesRecord setImageId(UUID value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.IMAGES.IMAGE_ID</code>.
     */
    @Column(name = "IMAGE_ID", nullable = false, precision = 2147483647)
    public UUID getImageId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>LOCATION_GAME.IMAGES.IMAGE</code>.
     */
    public ImagesRecord setImage(byte[] value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>LOCATION_GAME.IMAGES.IMAGE</code>.
     */
    @Column(name = "IMAGE", nullable = false, precision = 2147483647)
    @NotNull
    public byte[] getImage() {
        return (byte[]) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<UUID, byte[]> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<UUID, byte[]> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return Images.IMAGES.IMAGE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<byte[]> field2() {
        return Images.IMAGES.IMAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value1() {
        return getImageId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] value2() {
        return getImage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImagesRecord value1(UUID value) {
        setImageId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImagesRecord value2(byte[] value) {
        setImage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImagesRecord values(UUID value1, byte[] value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ImagesRecord
     */
    public ImagesRecord() {
        super(Images.IMAGES);
    }

    /**
     * Create a detached, initialised ImagesRecord
     */
    public ImagesRecord(UUID imageId, byte[] image) {
        super(Images.IMAGES);

        set(0, imageId);
        set(1, image);
    }
}
