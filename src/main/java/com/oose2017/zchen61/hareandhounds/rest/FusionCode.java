package com.oose2017.zchen61.hareandhounds.rest;

/**
 * Result Code for Requests
 */
public interface FusionCode {
    int GENERAL_ERROR = 400;
    /**
     * Game Start Response
     */
    interface StartGame {
        /**
         * Success
         */
        int REQ_SUC = 201;

        /**
         * Illegal pieceType
         */
        int REQ_FAIL = 400;
    }

    /**
     * Join Game Response
     */
    interface JoinGame {
        /**
         * Success
         */
        int REQ_SUC = 200;

        /**
         * Invalid game id
         */
        int REQ_FAIL_INVALID_ID = 404;
        /**
         * Second player already joined
         */
        int REQ_FAIL_PLAYER_FULL = 410;
    }

    /**
     * Join Game Response
     */
    interface GameBoard {
        /**
         * Success
         */
        int REQ_SUC = 200;

        /**
         * Invalid game id
         */
        int REQ_FAIL_INVALID_ID = 404;
    }
    /**
     * Play Game Response
     */
    interface PlayGame {
        /**
         * Success
         */
        int REQ_SUC = 200;

        /**
         * Invalid game id
         */
        int REQ_FAIL_INVALID_ID = 404;

        /**
         * Invalid player id
         */
        int INVALID_PLAYER_ID = 404;

        /**
         * Incorrect turn
         */
        int INCORRECT_TURN = 422;

        /**
         * Illegal move
         */
        int ILLEGAL_MOVE = 422;

    }
}
