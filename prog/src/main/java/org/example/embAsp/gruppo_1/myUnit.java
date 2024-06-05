package org.example.embAsp.gruppo_1;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;
import org.example.Game.mode.Unit;

import java.awt.*;
import java.util.Objects;

@Id("myUnit") //myUnit(X,Y,H,P)
public class myUnit {
    @Param(0)
    private int x;
    @Param(1)
    private int y;
    @Param(2)
    private int height;
    @Param(3)
    private int playerCode;

    public myUnit(int x, int y, int height, int playerCode) {
        init(x, y, height, playerCode);
    }
    public myUnit(Point coord, int height , int playerCode) {
         init(coord.x, coord.y, height, playerCode);
    }

    public myUnit(Unit unit, int height) {
        init(unit.coord().x, unit.coord().y,height, unit.player().getPlayerCode() );
    }
    public myUnit() {

    }

    private void init(int x, int y, int height, int playerCode) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.playerCode = playerCode;
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

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    //--UTILITIES-----------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        myUnit myUnit = (myUnit) o;
        return x == myUnit.x && y == myUnit.y && height == myUnit.height && playerCode == myUnit.playerCode ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, height, playerCode );
    }

    @Override
    public String toString() {
        return " unit(" +
                x +
                "," + y +
                "," + height +
                "," + playerCode +
                ") ";
    }
}

