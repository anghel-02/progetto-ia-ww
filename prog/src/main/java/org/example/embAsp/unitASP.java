package org.example.embAsp;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.util.Objects;

@Id("unit") //unit(X,Y)
public class unitASP {
    @Param(0)
    private int x;
    @Param(1)
    private int y;

    public unitASP(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public unitASP() {

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

    //--UTILITIES-----------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        unitASP unitASP = (unitASP) o;
        return x == unitASP.x && y == unitASP.y ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return " player(" +
                x +
                "," + y +
                ") ";
    }
}

