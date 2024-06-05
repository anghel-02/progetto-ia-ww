package org.example.Game.mode;

import java.awt.*;
import java.util.Objects;

public class Unit {
    final int unitCode;
    final Player player;
    final Point coord;

    public Unit(int unitCode, Player player, Point coord) {
        this.unitCode = unitCode;
        this.player = player.copy();
        this.coord = new Point(coord);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return unitCode == unit.unitCode && player.equals(unit.player) && coord.equals(unit.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitCode, player, coord);
    }
}
