package org.example.Game.mode;

import java.awt.*;

public class Unit {
    final int unitCode;
    final Player player;
    final Point coord;

    Unit(int unitCode, Player player, Point coord) {
        this.unitCode = unitCode;
        this.player = player;
        this.coord = coord;
    }


    public int unitCode() {
        return unitCode;
    }

    public Player player() {
        return player;
    }

    public Point coord() {
        return coord;
    }
}
