package org.example.Game;

import java.awt.*;

public record actionSet(Player player, Player.unit unit , Point move, Point build) {
    public actionSet {
        if (player.getPlayerCode() < 0 || player.getPlayerCode() > 1) {
            throw new IllegalArgumentException("playerCode must be 0 or 1");
        }

        if (move.x < 0 || move.x > Board.ROW_SIZE-1 || move.y < 0 || move.y > Board.COL_SIZE-1 ) {
            throw new IllegalArgumentException("move coordinates must be between 0" + (Board.ROW_SIZE-1)+"and" + (Board.COL_SIZE-1));
        }
    }

    public String display() {
        return "\nPlayer " + player.getSymbol() + " moves to ("+ move.x + "," + move.y +") and builds at (" + build.x + "," + build.y + ")";
    }

    @Override
    public String toString() {
        return " actionSet(" +
                player.getPlayerCode() +
                ", " +
                "(" + move.x + "," + move.y + ")" +
                "," +
                "("+build.x  + "," + build.y + ")"+
                ") ";
    }
}
