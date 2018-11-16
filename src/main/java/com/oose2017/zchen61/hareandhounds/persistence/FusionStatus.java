package com.oose2017.zchen61.hareandhounds.persistence;

/**
 * Status code and constants for POJO classes
 */
public interface FusionStatus {

    /**
     * Defines status of a player
     */
    interface Player {
        /**
         * player playing as hare
         */
        int PLAYER_TYPE_HARE = 100;

        /**
         * player playing as hound
         */
        int PLAYER_TYPE_HOUND = 101;
    }

    /**
     * Defines states of a game session
     */
    interface GameState {
        String WAITING_FOR_SECOND_PLAYER = "WAITING_FOR_SECOND_PLAYER";
        String TURN_HARE = "TURN_HARE";
        String TURN_HOUND = "TURN_HOUND";
        String WIN_HARE_BY_ESCAPE = "WIN_HARE_BY_ESCAPE";
        String WIN_HARE_BY_STALLING = "WIN_HARE_BY_STALLING";
        String WIN_HOUND = "WIN_HOUND";
    }

}
