package org.example.embAsp;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.util.Objects;

@Id("buildIn") //buildIn(x,y)
public class buildIn {
    @Param(0)
    private int x;
    @Param(1)
    private int y;

    public buildIn() {
    }
    public buildIn(int x, int y) {
        this.x = x;
        this.y = y;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        buildIn buildIn = (buildIn) o;
        return x == buildIn.x && y == buildIn.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return " buildIn(" +
                x +
                "," + y +
                ") ";
    }
}