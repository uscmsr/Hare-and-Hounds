package com.oose2017.zchen61.hareandhounds.rest.packet;

/**
 * Response for fetching game board.
 */
public class GameStateRsp {

    private String state;

    public GameStateRsp(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
