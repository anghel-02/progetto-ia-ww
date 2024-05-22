package org.example.embAsp;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.util.Objects;

@Id("player") //player(X,Y,P)
public class player {
    @Param(0)
    private int x;
    @Param(1)
    private int y;
    @Param(2)
    private int playerCode;

    public player(int x, int y, int playerCode) {
        this.x = x;
        this.y = y;
        this.playerCode = playerCode;
    }

    public player() {

    }
//--GETTERS AND SETTERS-------------------------------------------------------------------------------------------------
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPlayerCode() {
        return playerCode;
    }

    public void setPlayerCode(int playerCode) {
        this.playerCode = playerCode;
    }

//--UTILITIES-----------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        player player = (player) o;
        return x == player.x && y == player.y && playerCode == player.playerCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, playerCode);
    }

    @Override
    public String toString() {
        return " player(" +
                x +
                "," + y +
                "," + playerCode +
                ") ";
    }
}

