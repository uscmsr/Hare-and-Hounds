package com.oose2017.zchen61.hareandhounds.persistence;


/**
 * POJO Class for a player in a game session
 */
public class Player {

    /**
     * ID of the player, should be unique in a game session
     */
    private String id;

    /**
     * role of the player
     */
    private Integer type;

    /**
     * current X coordinate of the player
     */
    private Integer posX;
    /**
     * current Y coordinate of the player
     */
    private Integer posY;

    public Player(String id, int type, int x, int y) {
        this.id = id;
        this.type = type;
        this.posX = x;
        this.posY = y;
    }
//------------------------------------getter & setter------------------------------------------------------//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

}
