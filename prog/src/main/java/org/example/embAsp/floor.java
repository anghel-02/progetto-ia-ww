package org.example.embAsp;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.util.Objects;

@Id("floor") // floor(X,Y,H)
public class floor {
    @Param(0)
    private int x;
    @Param(1)
    private int y;
    @Param(2)
    private int height;

    public floor(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    public floor() {

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

    //--UTILITIES-----------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        floor floor = (floor) o;
        return x == floor.x && y == floor.y && height == floor.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, height);
    }

    @Override
    public String toString() {
        return " player(" +
                x +
                "," + y +
                "," + height +
                ") ";
    }
}
