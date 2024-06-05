package org.example.Game.mode;

import org.example.Game.mode.ai.PlayerAi;

import java.awt.*;

public class Unit {
    final int unitCode;
    final Player player;
    final Point coord;

    public Unit(int unitCode, Player player, Point coord) {
        this.unitCode = unitCode;
        this.player = player;
        this.coord = coord;
    }

    public Unit(Unit unit) {
        this.unitCode = unit.unitCode;
        this.player = unit.player;
        this.coord = new Point(unit.coord);
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
