//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.zchen61.hareandhounds.service;

import com.oose2017.zchen61.hareandhounds.bean.BoardPosition;
import com.oose2017.zchen61.hareandhounds.bean.Pair;
import com.oose2017.zchen61.hareandhounds.persistence.FusionStatus;
import com.oose2017.zchen61.hareandhounds.persistence.GameSession;
import com.oose2017.zchen61.hareandhounds.persistence.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides Controller detailed implementation of data operation
 * e.g. get update find ...
 */
public class GameService {
    public static final String PIECE_TYPE_HARE = "HARE";
    public static final String PIECE_TYPE_HOUND = "HOUND";

    /**
     * DAO Object ( you do not have to use a database to hold all your game data. You can do it all in memory.)
     */
    private List<GameSession> gameSessionDAO;

    private final Logger logger = LoggerFactory.getLogger(GameService.class);

    /**
     * Construct the model with a pre-defined datasource. The current implementation
     * also ensures that the "DAO" in memory is created if necessary.
     */
    public GameService() throws GameServiceException {
        gameSessionDAO = new ArrayList<>();
    }

    /**
     * Create a new GameSession entry.
     */
    public GameSession createNewGame(String pieceType) throws GameServiceException {
        // generate game ID
        String sessionID = UUID.randomUUID().toString();
        String playerID = UUID.randomUUID().toString();
        // determine player type
        int playerType;
        if (pieceType.equalsIgnoreCase(PIECE_TYPE_HARE)) {
            playerType = FusionStatus.Player.PLAYER_TYPE_HARE;
        } else if (pieceType.equalsIgnoreCase(PIECE_TYPE_HOUND)) {
            playerType = FusionStatus.Player.PLAYER_TYPE_HOUND;
        } else {
            throw new GameServiceException("Illegal pieceType", null);
        }
        // add game to DB
        GameSession game = new GameSession(sessionID, playerID, playerType);
        gameSessionDAO.add(game);
        return game;
    }

    /**
     * Find a GameSession by session ID.
     *
     * @param sessionId, returns null if not found
     */
    public GameSession findGameById(String sessionId) {
        for (GameSession session : gameSessionDAO) {
            if (session.getId().equals(sessionId)) {
                return session;
            }
        }
        return null;
    }

    /**
     * Add second player to a GameSession.
     *
     * @return [0]id of the second player; [1] type of second player
     */
    public String[] createSecondPlayer(GameSession session) throws GameServiceException {
        // generate player id
        String newPlayerId = UUID.randomUUID().toString();
        String[] result = new String[2];
        result[0] = newPlayerId;
        // determine player's role
        switch (session.getPlayer().size()) {
            case 1:
                // hosted by hare
                session.getPlayer().add(new Player(newPlayerId, FusionStatus.Player.PLAYER_TYPE_HOUND, 1, 0));
                session.getPlayer().add(new Player(newPlayerId, FusionStatus.Player.PLAYER_TYPE_HOUND, 0, 1));
                session.getPlayer().add(new Player(newPlayerId, FusionStatus.Player.PLAYER_TYPE_HOUND, 1, 2));
                result[1] = PIECE_TYPE_HOUND;
                break;
            case 3:
                // hosted by hounds
                session.getPlayer().add(new Player(newPlayerId, FusionStatus.Player.PLAYER_TYPE_HARE, 4, 1));
                result[1] = PIECE_TYPE_HARE;
                break;
            case 4:
                // already full
                throw new GameServiceException("Game is already full", null);
            default:
                throw new GameServiceException("Unidentified player number", null);
        }
        session.setState(FusionStatus.GameState.TURN_HOUND);// hound moving first
        // record initial board position
        BoardPosition thisPos = new BoardPosition(new Pair(4, 1).hashCode(), new Pair(1, 0).hashCode(),
                new Pair(0, 1).hashCode(), new Pair(1, 2).hashCode());
        session.getBoardPosRecord().put(thisPos, 1);
        return result;
    }

    /**
     * Execute a player's move on given GameSession and update relevant info on the game board.
     */
    public void updateGameStatus(GameSession session, String playerId, int fromX, int fromY, int toX, int toY)
            throws GameServiceException {
        // define all legal coordinates on the board
        List<Pair> allPossibleCoord = new ArrayList<>();
        List<Integer> allPossibleCoordHash = new ArrayList<>();
        int[][] ALL_POSSIBLE_COORDINATE = {{0, 1}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}, {3, 0}, {3, 1}, {3, 2}, {4, 1}};
        for (int[] p : ALL_POSSIBLE_COORDINATE) {
            allPossibleCoord.add(new Pair(p[0], p[1]));
            allPossibleCoordHash.add(new Pair(p[0], p[1]).hashCode());
        }
        // define all non-empty spots on the board
        List<Integer> allNonEmptyCoordHash = new ArrayList<>();
        for (Player p : session.getPlayer()) {
            allNonEmptyCoordHash.add(new Pair(p.getPosX(), p.getPosY()).hashCode());
        }

        // find all players with given id in the game
        List<Player> playersById = new ArrayList<>();
        for (Player p : session.getPlayer()) {
            if (p.getId().equals(playerId)) {
                playersById.add(p);
            }
        }
        if (playersById.isEmpty()) {
            throw new GameServiceException(GameServiceException.INVALID_PLAYER_ID, null);
        }
        // check turn
        if (playersById.get(0).getType() == FusionStatus.Player.PLAYER_TYPE_HOUND &&
                !session.getState().equals(FusionStatus.GameState.TURN_HOUND)) {
            logger.info("turn: hound, moving: hare");
            throw new GameServiceException(GameServiceException.INCORRECT_TURN, null);
        }
        if (playersById.get(0).getType() == FusionStatus.Player.PLAYER_TYPE_HARE &&
                !session.getState().equals(FusionStatus.GameState.TURN_HARE)) {
            logger.info("turn: hare, moving: hound");
            throw new GameServiceException(GameServiceException.INCORRECT_TURN, null);
        }
        // check player position - find player at (fromX, fromY)
        Player player = null;
        for (Player p : playersById) {
            if (p.getPosX() == fromX && p.getPosY() == fromY) {
                player = p;
                break;
            }
        }
        if (player == null) {
            logger.info("player at (" + fromX + "," + fromY + ") not found on map");
            throw new GameServiceException(GameServiceException.ILLEGAL_MOVE, null);
        }
        // check player position - only move to possible coordinate on the board
        int targetPosHash = new Pair(toX, toY).hashCode();
        if (!allPossibleCoordHash.contains(targetPosHash)) {
            logger.info("target pos (" + toX + "," + toY + ") is not on the map");
            throw new GameServiceException(GameServiceException.ILLEGAL_MOVE, null);
        }
        // check player position - only move to an empty location
        if (allNonEmptyCoordHash.contains(targetPosHash)) {
            logger.info("target pos (" + toX + "," + toY + ") is not empty");
            throw new GameServiceException(GameServiceException.ILLEGAL_MOVE, null);
        }
        // check player position - only move one step at a time
        if ((fromX == 1 && fromY == 1) || (fromX == 2 && fromY == 0) || (fromX == 2 && fromY == 2) || (fromX == 3 && fromY == 1)) {
            // from (1,1)(2,0)(2,2)(3,1), only 4 possible ways
            if (Math.abs(toX - fromX) + Math.abs(toY - fromY) > 1) {
                logger.info("no path exists from (" + fromX + "," + fromY + ") to (" + toX + "," + toY + ")");
                throw new GameServiceException(GameServiceException.ILLEGAL_MOVE, null);
            }
        } else {
            // from all other coordinate, 8 possible ways
            // can move to all 8 positions from center
            if (Math.abs(toX - fromX) > 1 || Math.abs(toY - fromY) > 1) {
                logger.info("from (" + fromX + "," + fromY + ") to (" + toX + "," + toY + ") > 1 step");
                throw new GameServiceException(GameServiceException.ILLEGAL_MOVE, null);
            }
        }

        // check player position - Hounds cannot move backwards
        if (player.getType() == FusionStatus.Player.PLAYER_TYPE_HOUND && toX < fromX) {
            throw new GameServiceException(GameServiceException.ILLEGAL_MOVE, null);
        }
        // perform move
        player.setPosX(toX);
        player.setPosY(toY);
        // record new board position
        List<Integer> newPosHash = new ArrayList<>(4);
        for (Player p : session.getPlayer()) {
            if (p.getType().equals(FusionStatus.Player.PLAYER_TYPE_HARE)) {
                newPosHash.add(0, new Pair(p.getPosX(), p.getPosY()).hashCode());
            } else {
                newPosHash.add(new Pair(p.getPosX(), p.getPosY()).hashCode());
            }
        }
        BoardPosition newPosObj = new BoardPosition(newPosHash.get(0),
                newPosHash.get(1), newPosHash.get(2), newPosHash.get(3));
        BoardPosition samePosFound = null;
        for (BoardPosition p : session.getBoardPosRecord().keySet()) {
            if (p.compareTo(newPosObj) == 0) {
                samePosFound = p;
                break;
            }
        }
        // check game status change - same board position occurs three times over the course of the game
        if (samePosFound == null) {
            // current position has never occurred
            session.getBoardPosRecord().put(newPosObj, 1);
        } else {
            // current position has occurred at least once

            session.getBoardPosRecord().put(samePosFound, session.getBoardPosRecord().get(samePosFound) + 1);
            if (session.getBoardPosRecord().get(samePosFound) >= 3) {
                session.setState(FusionStatus.GameState.WIN_HARE_BY_STALLING);
                return;
            }
        }
        // check game status change - hare has no valid move
        List<Integer> houndXPos = new ArrayList<>(3);
        List<Integer> houndYPos = new ArrayList<>(3);
        int hareXPos = -1, hareYPos = -1;
        for (Player p : session.getPlayer()) {
            if (p.getType() == FusionStatus.Player.PLAYER_TYPE_HOUND) {
                houndXPos.add(p.getPosX());
                houndYPos.add(p.getPosY());
            } else {
                hareXPos = p.getPosX();
                hareYPos = p.getPosY();
            }
        }
        if (houndXPos.get(0) == 3 && houndXPos.get(1) == 3 && houndXPos.get(2) == 3) {
            session.setState(FusionStatus.GameState.WIN_HOUND);
            return;
        }
        // check game status change - no hounds are at the left of the hare
        boolean isHareWinByEscape = false;
        for (int i = 0; i < houndXPos.size(); i++) {
            if (houndXPos.get(i) < hareXPos) {
                isHareWinByEscape = false;
                break;
            } else {
                isHareWinByEscape = true;
            }
        }
        if (isHareWinByEscape) {
            session.setState(FusionStatus.GameState.WIN_HARE_BY_ESCAPE);
            return;
        }

        // Game is not over, switch turn
        if (session.getState().equals(FusionStatus.GameState.TURN_HOUND)) {
            session.setState(FusionStatus.GameState.TURN_HARE);
        } else {
            session.setState(FusionStatus.GameState.TURN_HOUND);
        }
    }
    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    public static class GameServiceException extends Exception {
        // description of errors
        public static final String INVALID_GAME_ID = "INVALID_GAME_ID";
        public static final String INVALID_PLAYER_ID = "INVALID_PLAYER_ID";
        public static final String INCORRECT_TURN = "INCORRECT_TURN";
        public static final String ILLEGAL_MOVE = "ILLEGAL_MOVE";

        public GameServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
