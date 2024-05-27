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
    //--CHOSE UNIT
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

    //--CHOSE ENCONDING
        handler.addInputProgram(player.getEncodings().getFirst());

    //--REFRESH GRID STATE
        gridState(player, handler, myUnit);

    //--SET HANDLER
        player.setHandler( handler);
    }
    private static void gruppo2(PlayerAi player) throws Exception {
        //--CHOSE UNIT
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

        handler = player.getHandler();

        //--CHOSE ENCONDING
        handler.addInputProgram(player.getEncodings().getFirst());

        //--REFRESH GRID STATE
        gridState(player, handler, myUnit);

        //--SET HANDLER
        player.setHandler( handler);
    }

    private static void gruppo3(PlayerAi player) throws Exception {
        //--CHOSE UNIT
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

        handler = player.getHandler();

        //--CHOSE ENCONDING
        handler.addInputProgram(player.getEncodings().getFirst());

        //--REFRESH GRID STATE
        gridState(player, handler, myUnit);

        //--SET HANDLER
        player.setHandler( handler);
    }
    private static void gruppo4(PlayerAi player) throws Exception {
        //--CHOSE UNIT
        Player.unit myUnit = player.getUnits().getFirst();
        WondevWomanHandler handler = new WondevWomanHandler();

        handler = player.getHandler();

        //--CHOSE ENCONDING
        handler.addInputProgram(player.getEncodings().getFirst());

        //--REFRESH GRID STATE
        gridState(player, handler, myUnit);

        //--SET HANDLER
        player.setHandler( handler);
    }

//--UTILITY METHODS-----------------------------------------------------------------------------------------------------
    private static void gridState(PlayerAi player, WondevWomanHandler handler, Player.unit myUnit) throws Exception {
    //--ADD PLAYER
        handler.addFactAsString("player("+player.getPlayerCode()+")");

    //--ADD PLAYABLE CELLS
        for (Point coord: GameHandler.getBoard().playableArea(myUnit.unitCode()) ){
            handler.addFactAsObject(new cell(coord.x, coord.y));
        }

    //--ADD UNITS
        for (Player p : GameHandler.getBoard().getPlayers() ){
            for (Player.unit unit : p.getUnits()){
                handler.addFactAsObject(new unitASP(unit.coord().x, unit.coord().y, p.getPlayerCode()));
            }
        }

    //--ADD FLOORS
        int [][] grid = GameHandler.getBoard().getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                handler.addFactAsObject(new floor(i, j, grid[i][j]));
            }
        }

    }


}
