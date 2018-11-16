//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.zchen61.hareandhounds.persistence;

import com.oose2017.zchen61.hareandhounds.bean.BoardPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oose2017.zchen61.hareandhounds.persistence.FusionStatus.Player.PLAYER_TYPE_HARE;
import static com.oose2017.zchen61.hareandhounds.persistence.FusionStatus.Player.PLAYER_TYPE_HOUND;

/**
 * POJO Class for a game session
 */
public class GameSession {

    private final Logger logger = LoggerFactory.getLogger(GameSession.class);

    /**
     * Game ID, unique across all games
     */
    private String id;

    /**
     * State of the game
     */
    private String state;

    /**
     * Player Objects, 3 hound objects share the same playerID
     */
    private List<Player> player;

    /**
     * Hashmap used to record every board position occurence
     */
    private Map<BoardPosition, Integer> boardPosRecord;


    public GameSession(String id, String playerID, int playerType) {
        this.id = id;
        boardPosRecord = new HashMap<>();
        player = new ArrayList<>(4);
        state = FusionStatus.GameState.WAITING_FOR_SECOND_PLAYER;
        // create player object
        switch (playerType) {
            case PLAYER_TYPE_HARE:
                player.add(new Player(playerID, PLAYER_TYPE_HARE, 4,1));
                break;
            case PLAYER_TYPE_HOUND:
                player.add(new Player(playerID, PLAYER_TYPE_HOUND,1,0));
                player.add(new Player(playerID, PLAYER_TYPE_HOUND,0,1));
                player.add(new Player(playerID, PLAYER_TYPE_HOUND,1,2));
                break;
            default:
                logger.info("GameSession constructor - illegal playerType");
                throw new IllegalArgumentException();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Player> getPlayer() {
        return player;
    }

    public Map<BoardPosition, Integer> getBoardPosRecord() {
        return boardPosRecord;
    }
}
