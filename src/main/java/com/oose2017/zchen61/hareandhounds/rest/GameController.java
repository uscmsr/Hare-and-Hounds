//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.zchen61.hareandhounds.rest;

import com.google.gson.Gson;
import com.oose2017.zchen61.hareandhounds.persistence.FusionStatus;
import com.oose2017.zchen61.hareandhounds.persistence.GameSession;
import com.oose2017.zchen61.hareandhounds.persistence.Player;
import com.oose2017.zchen61.hareandhounds.rest.packet.*;
import com.oose2017.zchen61.hareandhounds.service.GameService;
import com.oose2017.zchen61.hareandhounds.util.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

/**
 * REST layer
 * this key class dispatches the different routes/endpoints
 */
public class GameController {

    private static final String API_CONTEXT = "/hareandhounds/api";

    private final GameService gameService;

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    public GameController(GameService gameService) {
        this.gameService = gameService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        // Start a game
        post(API_CONTEXT + "/games", "application/json", (request, response) -> {
            try {
                GameStartReq req = new Gson().fromJson(request.body(), GameStartReq.class);
                GameSession game = gameService.createNewGame(req.getPieceType());
                response.status(FusionCode.StartGame.REQ_SUC);
                return new GameStartJoinRsp(game.getId(), game.getPlayer().get(0).getId(), req.getPieceType());
            } catch (GameService.GameServiceException ex) {
                response.status(FusionCode.StartGame.REQ_FAIL);
                return new ErrorRsp(ex.toString());
            } catch (Exception ex) {
                response.status(FusionCode.GENERAL_ERROR);
                return new ErrorRsp("Malformed Request");
            }
        }, new JsonTransformer());

        // Join a game
        put(API_CONTEXT + "/games/:id", "application/json", (request, response) -> {
            GameSession session = gameService.findGameById(request.params(":id"));
            if (session == null) {
                // game id does not exist
                response.status(FusionCode.JoinGame.REQ_FAIL_INVALID_ID);
                return new ErrorRsp("Invalid game id");
            }
            if (session.getPlayer().size() >= 4) {
                // Second player already joined
                response.status(FusionCode.JoinGame.REQ_FAIL_PLAYER_FULL);
                return new ErrorRsp("Second player already joined");
            }
            try {
                String[] rsp = gameService.createSecondPlayer(session);
                return new GameStartJoinRsp(session.getId(), rsp[0], rsp[1]);
            } catch (Exception ex) {
                response.status(FusionCode.GENERAL_ERROR);
                return new ErrorRsp(ex.toString());
            }
        }, new JsonTransformer());

        // Describe the game board
        get(API_CONTEXT + "/games/:id/board", "application/json", (request, response) -> {

            GameSession session = gameService.findGameById(request.params(":id"));
            if (session == null) {
                // game id does not exist
                response.status(FusionCode.JoinGame.REQ_FAIL_INVALID_ID);
                return new ErrorRsp("Invalid game id");
            }
            List<GameBoardRsp> rsp = new ArrayList<>();
            try {
                for (Player p : session.getPlayer()) {
                    String type = (p.getType() == FusionStatus.Player.PLAYER_TYPE_HOUND ? "HOUND" : "HARE");
                    rsp.add(new GameBoardRsp(type, p.getPosX(), p.getPosY()));
                }
                return rsp;
            } catch (Exception ex) {
                response.status(FusionCode.GENERAL_ERROR);
                return new ErrorRsp(ex.toString());
            }
        }, new JsonTransformer());

        // Describe the game state
        get(API_CONTEXT + "/games/:id/state", "application/json", (request, response) -> {

            GameSession session = gameService.findGameById(request.params(":id"));
            if (session == null) {
                // game id does not exist
                response.status(FusionCode.JoinGame.REQ_FAIL_INVALID_ID);
                return new ErrorRsp("Invalid game id");
            }
            try {
                GameStateRsp rsp = new GameStateRsp(session.getState());
                return rsp;
            } catch (Exception ex) {
                response.status(FusionCode.GENERAL_ERROR);
                return new ErrorRsp(ex.toString());
            }
        }, new JsonTransformer());

        // Play a game
        post(API_CONTEXT + "/games/:id/turns", "application/json", (request, response) -> {
            // find game session by id
            GameSession session = gameService.findGameById(request.params(":id"));
            if (session == null) {
                // game id does not exist
                response.status(FusionCode.PlayGame.REQ_FAIL_INVALID_ID);
                return new ErrorRsp(GameService.GameServiceException.INVALID_GAME_ID);
            }
            try {
                // parse json request
                GamePlayReq req = new Gson().fromJson(request.body(), GamePlayReq.class);
                // attempt to update the board
                gameService.updateGameStatus(session, req.getPlayerId(),
                        req.getFromX(), req.getFromY(), req.getToX(), req.getToY());
                response.status(FusionCode.PlayGame.REQ_SUC);
                return new GamePlayRsp(req.getPlayerId());
            } catch (GameService.GameServiceException ex) {
                if (ex.getMessage().equalsIgnoreCase(GameService.GameServiceException.INVALID_PLAYER_ID)) {
                    response.status(FusionCode.PlayGame.INVALID_PLAYER_ID);
                    return new ErrorRsp(GameService.GameServiceException.INVALID_PLAYER_ID);
                } else if (ex.getMessage().equalsIgnoreCase(GameService.GameServiceException.ILLEGAL_MOVE)) {
                    response.status(FusionCode.PlayGame.ILLEGAL_MOVE);
                    return new ErrorRsp(GameService.GameServiceException.ILLEGAL_MOVE);
                } else if (ex.getMessage().equalsIgnoreCase(GameService.GameServiceException.INCORRECT_TURN)) {
                    response.status(FusionCode.PlayGame.INCORRECT_TURN);
                    return new ErrorRsp(GameService.GameServiceException.INCORRECT_TURN);
                }
                response.status(FusionCode.GENERAL_ERROR);
                return new ErrorRsp(ex.toString());
            } catch (Exception ex) {
                response.status(FusionCode.GENERAL_ERROR);
                return new ErrorRsp("Malformed Request");
            }
        }, new JsonTransformer());
    }
}
