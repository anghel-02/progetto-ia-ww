package org.example.embAsp.gruppo_1;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.example.Game.gameManager.Board;
import org.example.Game.gameManager.GameHandler;
import org.example.Game.mode.Player;
import org.example.Game.mode.Unit;
import org.example.Game.mode.ai.NullAction;
import org.example.Game.mode.ai.PlayerAi;
import org.example.Game.mode.ai.actionSet;
import org.example.Settings;
import org.example.embAsp.*;

import java.awt.*;
import java.util.ArrayList;


public class Group1 implements Group {
    private Board myBoard;
    private PlayerAi myPlayer;
    private WondevWomanHandler myHandler;
    private Unit myUnit;

    /**
     * Call the EmbAsp for the player.
     * Use a copy of the board and the player to avoid any modification.<p>
     * After the computation, the method will return the actionSet to be executed.
     * @param player
     * @returns
     * @throws Exception
     */
    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = GameHandler.getBoard().copy();
        myPlayer = (PlayerAi) myBoard.getPlayers()[player.getPlayerCode()];
    //--SETTING HANDLER
        myHandler = new WondevWomanHandler(player.getHandler());

    //--CHOSE UNIT
        myUnit = myPlayer.getFirstUnit();
        //TODO: implementare height nella classe myUnit
        myHandler.addFactAsObject(new myUnit(myUnit, myBoard.heightAt(myUnit.coord())));

    //--MOVE
        Point move = makeMove();


        if(move == null)
            return new NullAction(myUnit);
        //MOVE IN BOARD
        if (! myBoard.moveUnitSafe(myUnit, move))
            throw new RuntimeException("SOMETHING WRONG, CANNOT MOVE unit "+myUnit.unitCode()+" IN GROUP. "+
                    "("+myUnit.coord().x+","+myUnit.coord().y+")"+
                    "-->"+ "("+move.x +","+ move.y+")");

        //REFRESH FACTS
        refreshGridState();

    //--BUILD
        Point build = makeBuild();

        if(build == null)
            return new NullAction(myUnit);

        return new actionSet(player.getFirstUnit(), move, build);
    }

    private Point makeMove() throws Exception {
    //TODO: CAMBIARE METODO SCELTA ENCODING, MOLTO VULNERBILE USANDO INDICI

//        myHandler.setEncoding(myPlayer.getEncodings().get(0)); linux bastardo
        ASPInputProgram moveProgram = new ASPInputProgram();
        moveProgram.addFilesPath(Settings.PATH_ENCOD_GROUP1+ "/move.asp");
        myHandler.setEncoding(moveProgram);


    //--CAN'T MOVE
        //moveCell(X,Y). --> cell where I can move unit
        ArrayList<Point> moveableArea = myBoard.moveableArea(myUnit);
        if (moveableArea.isEmpty()) {
            return null;
        }

    //--CAN MOVE
        for (Point cell : moveableArea)
            myHandler.addFactAsString("moveCell(" + cell.x + "," + cell.y +","+ myBoard.heightAt(cell) +")");

//

        myHandler.startSync();
        //ADDING FACTS
        for (Object atom : myHandler.getOptimalAnswerSets().getFirst().getAtoms()) {
            if (atom instanceof moveIn){
                Point moveIn = new Point(((moveIn) atom).getX(), ((moveIn) atom).getY());
                testOptimalMove(moveIn, myBoard.getGrid(), moveableArea);
                return moveIn;
            }
        }

        throw new RuntimeException("Something wrong in makeMove");
    }

    //TODO: ATTENZIONE I FATTI AGGIUNTI DA MOVE NON VENGONO CANCELLATI, MA PER ORA NON VANNO IN CONFLITTO
    private Point makeBuild() throws Exception {
//        myHandler.setEncoding(myPlayer.getEncodings().get(1)); linux
        ASPInputProgram buildProgram = new ASPInputProgram();
        buildProgram.addFilesPath(Settings.PATH_ENCOD_GROUP1+ "/build.asp");
        myHandler.setEncoding(buildProgram);
    //--CAN'T BUILD
        //moveCell(X,Y). --> cell where I can move unit
        ArrayList<Point> buildableArea = myBoard.buildableArea(myUnit);
        if (buildableArea.isEmpty()) {
            return null;
        }

    //--CAN BUILD
        for (Point cell : buildableArea)
            myHandler.addFactAsString("buildCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell)+ ")");

        myHandler.startSync();
//        System.out.println(myHandler.getFactsString());
        //ADDING FACTS
        for (Object atom : myHandler.getOptimalAnswerSets().getFirst().getAtoms()) {
            if (atom instanceof buildIn)
                return new Point(((buildIn) atom).getX(), ((buildIn) atom).getY());
        }

        throw new RuntimeException("Something wrong in makeBuild");
    }


//--UTILITY-------------------------------------------------------------------------------------------------------------

    /**
     * Refresh facts in myHandler without calling PlayerAi.refreshGridstate().
     */
    private void refreshGridState() throws Exception {
        ASPInputProgram myGridState = new ASPInputProgram();
        int [][] grid = myBoard.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                myGridState.addObjectInput((new cell(i, j, grid[i][j], myBoard.playerCodeAt(i, j))));
            }
        }
        myHandler.setFactProgram(myGridState);
        myHandler.addFactAsObject(new myUnit(myUnit, myBoard.heightAt(myUnit.coord())));
    }

//--TEST----------------------------------------------------------------------------------------------------------------
    //TODO: ELIMINARE DOPO FASE SVILUPPO
    //TEST IF UNIT DOESN'T MOVE TO A CELL WITH HEIGHT 3
    private void testOptimalMove(Point move, int[][] grid, ArrayList<Point> moveAbleArea){
        if (grid[move.x][move.y] != 3 ){
            for (Point cell : moveAbleArea)
                if (grid[cell.x][cell.y] == 3 && ! move.equals(cell))
                    throw new RuntimeException("TEST FAILED, Move Not Optimal. Group1 move to ("+move.x+","+move.y+")"+
                            " instead of ("+cell.x+","+cell.y+")");
        }


    }

//--
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group1 group1 = (Group1) o;
        return myBoard.equals(group1.myBoard) && myPlayer.equals(group1.myPlayer) && myHandler.equals(group1.myHandler) && myUnit.equals(group1.myUnit);
    }

}
