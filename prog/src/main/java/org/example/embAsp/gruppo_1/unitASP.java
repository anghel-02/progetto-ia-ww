package org.example.embAsp.gruppo_1;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;
import org.example.Game.mode.Unit;

import java.awt.*;
import java.util.Objects;

@Id("unit") //unit(X,Y,H,U,P)
public class unitASP {
    @Param(0)
    private int x;
    @Param(1)
    private int y;
    @Param(2)
    private int height;
    @Param(3)
    private int unitCode;
    @Param(4)
    private int playerCode;

    public unitASP(int x, int y, int height, int unitCode, int playerCode) {

        init(x, y, height, unitCode, playerCode);
    }
    public unitASP(Point coord, int height , int unitCode, int playerCode ) {

        init(coord.x, coord.y, height, unitCode, playerCode);
    }

    public unitASP(Unit unit, int height) {
        init(unit.coord().x, unit.coord().y,height, unit.unitCode() , unit.player().getPlayerCode() );
    }
    public unitASP() {

    }

    private void init(int x, int y, int height, int unitCode, int playerCode) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.unitCode = unitCode;
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


    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
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
        unitASP that = (unitASP) o;
        return x == that.x && y == that.y && height == that.height && unitCode== that.unitCode && playerCode == that.playerCode ;
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
                "," + unitCode +
                "," + playerCode +
                ") ";
    }
}

