package com.oose2017.zchen61.hareandhounds.rest.packet;

/**
 * Request for game start. Used to map JSON input.
 */
public class GameStartReq {
    private String pieceType;

    public GameStartReq(String pieceType) {
        this.pieceType = pieceType;
    }

    public String getPieceType() {
        return pieceType;
    }

    public void setPieceType(String pieceType) {
        this.pieceType = pieceType;
    }
}
