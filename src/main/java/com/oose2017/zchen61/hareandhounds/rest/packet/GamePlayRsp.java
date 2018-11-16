package com.oose2017.zchen61.hareandhounds.rest.packet;

/**
 * Response for fetching game board.
 */
public class GamePlayRsp {

    private String playerId;

    public GamePlayRsp(String playerId) {
        this.playerId = playerId;
    }


    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
