/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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
public class Team implements Serializable {

    private static final long serialVersionUID = 1271377409;

    private String  name;
    private Integer gameId;
    private Integer color;

    public Team() {}

    public Team(Team value) {
        this.name = value.name;
        this.gameId = value.gameId;
        this.color = value.color;
    }

    public Team(
        String  name,
        Integer gameId,
        Integer color
    ) {
        this.name = name;
        this.gameId = gameId;
        this.color = color;
    }

    @Column(name = "NAME", nullable = false, length = 2147483647)
    @NotNull
    @Size(max = 2147483647)
    public String getName() {
        return this.name;
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "GAME_ID", nullable = false, precision = 10)
    @NotNull
    public Integer getGameId() {
        return this.gameId;
    }

    public Team setGameId(Integer gameId) {
        this.gameId = gameId;
        return this;
    }

    @Column(name = "COLOR", nullable = false, precision = 10)
    @NotNull
    public Integer getColor() {
        return this.color;
    }

    public Team setColor(Integer color) {
        this.color = color;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Team (");

        sb.append(name);
        sb.append(", ").append(gameId);
        sb.append(", ").append(color);

        sb.append(")");
        return sb.toString();
    }
}
