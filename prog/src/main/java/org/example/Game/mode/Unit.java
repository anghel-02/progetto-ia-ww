package org.example.Game.mode;

import java.awt.*;

public class Unit {
    final int unitCode;
    final int playerCode;
    final Point coord;

    Unit(int unitCode, int playerCode, Point coord) {
        this.unitCode = unitCode;
        this.playerCode = playerCode;
        this.coord = coord;
    }

    public int unitCode() {
        return unitCode;
    }

    public int playerCode() {
        return playerCode;
    }

    public Point coord() {
        return coord;
    }
}
