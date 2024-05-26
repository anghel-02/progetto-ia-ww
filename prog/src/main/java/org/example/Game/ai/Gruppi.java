package org.example.Game.ai;

import org.example.Game.GameHandler;

import org.example.Game.Player;
import org.example.embAsp.WondevWomanHandler;
import org.example.embAsp.cell;
import org.example.embAsp.floor;
import org.example.embAsp.unitASP;

import java.awt.*;
//TODO: vedere se conviene fare singleton
public class Gruppi {
    static void call(PlayerAi player) throws Exception {
        switch (player.getGroupID()){
            case 1 -> gruppo1(player);
            case 2 -> gruppo2(player);
            case 3 -> gruppo3(player);
            case 4 -> gruppo4(player);
            default -> throw new IllegalArgumentException("Group not found");
        }
    }

    private static void gruppo1(PlayerAi player) throws Exception {
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

        handler = player.getHandler();


        handler.addInputProgram(player.getEncodings().getFirst());


        for (Point coord: GameHandler.getBoard().playableArea(myUnit.unitCode()) ){
            handler.addFactAsObject(new cell(coord.x, coord.y));
        }

        //--ADD FLOORS
        int [][] grid = GameHandler.getBoard().getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                handler.addFactAsObject(new floor(i, j, grid[i][j]));
                //--ADD UNITS
                if (GameHandler.getBoard().isOccupied(i, j)){
                    handler.addFactAsObject(new unitASP(i, j));
                }
            }
        }

        player.setHandler( handler);
    }
    private static void gruppo2(PlayerAi player) throws Exception {
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

        handler = player.getHandler();


        handler.addInputProgram(player.getEncodings().getFirst());


        for (Point coord: GameHandler.getBoard().playableArea(myUnit.unitCode()) ){
            handler.addFactAsObject(new cell(coord.x, coord.y));
        }

        //--ADD FLOORS
        int [][] grid = GameHandler.getBoard().getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                handler.addFactAsObject(new floor(i, j, grid[i][j]));
                //--ADD UNITS
                if (GameHandler.getBoard().isOccupied(i, j)){
                    handler.addFactAsObject(new unitASP(i, j));
                }
            }
        }

        player.setHandler( handler);
    }

    private static void gruppo3(PlayerAi player) throws Exception {
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

        handler = player.getHandler();


        handler.addInputProgram(player.getEncodings().getFirst());


        for (Point coord: GameHandler.getBoard().playableArea(myUnit.unitCode()) ){
            handler.addFactAsObject(new cell(coord.x, coord.y));
        }

        //--ADD FLOORS
        int [][] grid = GameHandler.getBoard().getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                handler.addFactAsObject(new floor(i, j, grid[i][j]));
                //--ADD UNITS
                if (GameHandler.getBoard().isOccupied(i, j)){
                    handler.addFactAsObject(new unitASP(i, j));
                }
            }
        }

        player.setHandler( handler);
    }
    private static void gruppo4(PlayerAi player) throws Exception {
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

        handler = player.getHandler();


        handler.addInputProgram(player.getEncodings().getFirst());


        for (Point coord: GameHandler.getBoard().playableArea(myUnit.unitCode()) ){
            handler.addFactAsObject(new cell(coord.x, coord.y));
        }

        //--ADD FLOORS
        int [][] grid = GameHandler.getBoard().getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                handler.addFactAsObject(new floor(i, j, grid[i][j]));
                //--ADD UNITS
                if (GameHandler.getBoard().isOccupied(i, j)){
                    handler.addFactAsObject(new unitASP(i, j));
                }
            }
        }

        player.setHandler( handler);
    }


}
