package com.oose2017.zchen61.hareandhounds.rest.packet;

import java.util.ArrayList;
import java.util.List;

/**
 * Response for fetching game board.
 */
public class GameBoardRsp {

    private String pieceType;
    private int x;
    private int y;

    public GameBoardRsp(String pieceType, int x, int y) {
        this.pieceType = pieceType;
        this.x = x;
        this.y = y;
    }

    public String getPieceType() {
        return pieceType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
