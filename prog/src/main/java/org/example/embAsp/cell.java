package org.example.embAsp;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import javax.naming.InvalidNameException;
import java.util.Objects;

@Id("cell") //%cell(X,Y,H,P))
public class cell {
    @Param(0)
    private int x;
    @Param(1)
    private int y;
    @Param(2)
    private int height;
    @Param(3)
    private int playerCode;
    public static final int PLAYERCODE_NO_PLAYER = -1;

    public cell() {
    }

    public cell(int x, int y, int height, int playerCode) throws IllegalArgumentException {
        this.x = x;
        this.y = y;
        setHeight(height);
        setPlayerCode(playerCode);
    }

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

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        if (height < 0 || height > 4)
            throw new IllegalArgumentException("Invalid height, must be between 0 and 4");
        this.height = height;
    }

    public int getPlayerCode() {
        return playerCode;
    }
    public void setPlayerCode(int playerCode) throws IllegalArgumentException {
        if(playerCode < -1 || playerCode > 1)
            throw new IllegalArgumentException("Invalid player code, must be {-1(No Player),0,1}");
        this.playerCode = playerCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        cell cell = (cell) o;
        return x == cell.x && y == cell.y && height == cell.height && playerCode == cell.playerCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, height, playerCode);
    }

    @Override
    public String toString() {
        return " cell(" +
                x +
                "," + y +
                "," + height +
                "," + playerCode +
                ") ";
    }



}
