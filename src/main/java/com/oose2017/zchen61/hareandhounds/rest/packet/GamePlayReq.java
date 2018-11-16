package com.oose2017.zchen61.hareandhounds.rest.packet;

/**
 * Request for game start. Used to map JSON input.
 */
public class GamePlayReq {
    private String playerId;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public GamePlayReq(String playerId, int fromX, int fromY, int toX, int toY) {
        this.playerId = playerId;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToX() {
        return toX;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }
}
