package com.oose2017.zchen61.hareandhounds.rest.packet;

/**
 * Response for start/join game request.
 */
public class GameStartJoinRsp {
    private String pieceType;
    private String playerId;
    private String gameId;

    public GameStartJoinRsp(String gameId, String playerId, String pieceType) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.pieceType = pieceType;
    }

    public String getPieceType() {
        return pieceType;
    }

    public void setPieceType(String pieceType) {
        this.pieceType = pieceType;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
