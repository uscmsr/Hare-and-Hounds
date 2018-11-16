package com.oose2017.zchen61.hareandhounds.bean;

import com.oose2017.zchen61.hareandhounds.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Gameboard position class. Used to record positions of each turn.
 */
public class BoardPosition implements Comparable {

    private final Logger logger = LoggerFactory.getLogger(GameService.class);
    /**
     * hash value of hare's position
     */
    private int posHashHare;
    /**
     * A sorted set of the 3 hounds' hashed positions.
     */
    private ArrayList<Integer> posHashHound;

    /**
     * Record a board position
     *
     * @param posHashHare   Hash value of the hare's coordinate
     * @param posHashHound1 Hash value of the hound's coordinate
     */
    public BoardPosition(int posHashHare, int posHashHound1, int posHashHound2, int posHashHound3) {
        this.posHashHare = posHashHare;
        this.posHashHound = new ArrayList<>();
        this.posHashHound.add(posHashHound1);
        this.posHashHound.add(posHashHound2);
        this.posHashHound.add(posHashHound3);
        Collections.sort(this.posHashHound);
    }


    /**
     * Compare two board positions
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof BoardPosition)) {
            throw new ClassCastException("can only compare with BoardPosition object");
        }

        boolean isEqual = false;
        // compare hare
        logger.info("Compare Hare: " + ((BoardPosition) o).posHashHare + "(new), " + posHashHare + "(exist)");
        if (((BoardPosition) o).posHashHare != posHashHare) {
            return -1;
        }
        // compare hound
        for (int i = 0; i < posHashHound.size(); i++) {
            logger.info("Compare Hound: " + ((BoardPosition) o).posHashHound.get(i) + "(new), " + posHashHound.get(i) + "(exist)");

            if (posHashHound.get(i).equals(((BoardPosition) o).posHashHound.get(i))) {
                isEqual = true;
            } else {
                isEqual = false;
                return -1;
            }
        }
        return (isEqual ? 0 : -1);
    }

}