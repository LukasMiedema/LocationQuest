/**
 * This class is generated by jOOQ
 */
package nl.lukasmiedema.locationquest.entity;


import javax.annotation.Generated;

import nl.lukasmiedema.locationquest.entity.tables.AnsweredQuestion;
import nl.lukasmiedema.locationquest.entity.tables.Game;
import nl.lukasmiedema.locationquest.entity.tables.Images;
import nl.lukasmiedema.locationquest.entity.tables.MultipleChoiceAnswer;
import nl.lukasmiedema.locationquest.entity.tables.Player;
import nl.lukasmiedema.locationquest.entity.tables.Question;
import nl.lukasmiedema.locationquest.entity.tables.Team;
import nl.lukasmiedema.locationquest.entity.tables.TeamPlayer;


/**
 * Convenience access to all tables in LOCATION_GAME
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>LOCATION_GAME.PLAYER</code>.
     */
    public static final Player PLAYER = nl.lukasmiedema.locationquest.entity.tables.Player.PLAYER;

    /**
     * The table <code>LOCATION_GAME.GAME</code>.
     */
    public static final Game GAME = nl.lukasmiedema.locationquest.entity.tables.Game.GAME;

    /**
     * The table <code>LOCATION_GAME.TEAM</code>.
     */
    public static final Team TEAM = nl.lukasmiedema.locationquest.entity.tables.Team.TEAM;

    /**
     * The table <code>LOCATION_GAME.TEAM_PLAYER</code>.
     */
    public static final TeamPlayer TEAM_PLAYER = nl.lukasmiedema.locationquest.entity.tables.TeamPlayer.TEAM_PLAYER;

    /**
     * The table <code>LOCATION_GAME.QUESTION</code>.
     */
    public static final Question QUESTION = nl.lukasmiedema.locationquest.entity.tables.Question.QUESTION;

    /**
     * The table <code>LOCATION_GAME.MULTIPLE_CHOICE_ANSWER</code>.
     */
    public static final MultipleChoiceAnswer MULTIPLE_CHOICE_ANSWER = nl.lukasmiedema.locationquest.entity.tables.MultipleChoiceAnswer.MULTIPLE_CHOICE_ANSWER;

    /**
     * The table <code>LOCATION_GAME.ANSWERED_QUESTION</code>.
     */
    public static final AnsweredQuestion ANSWERED_QUESTION = nl.lukasmiedema.locationquest.entity.tables.AnsweredQuestion.ANSWERED_QUESTION;

    /**
     * The table <code>LOCATION_GAME.IMAGES</code>.
     */
    public static final Images IMAGES = nl.lukasmiedema.locationquest.entity.tables.Images.IMAGES;
}
