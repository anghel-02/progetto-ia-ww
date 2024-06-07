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
    private PlayerAi enemyPlayer;
    private WondevWomanHandler myHandler;
    private Unit myUnit;
    private ArrayList<Unit> enemyUnits ;

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

    //--SET PLAYERS
        for (Player p : myBoard.getPlayers() ){
            if (p.getPlayerCode() == player.getPlayerCode())
                myPlayer =(PlayerAi) p;
            else
                enemyPlayer = (PlayerAi) p;
        }

    //--SET HANDLER
        myHandler = new WondevWomanHandler(player.getHandler());
        myHandler.mapToEmb(unitASP.class);

    //--CHOSE UNIT
        myUnit = myPlayer.getFirstUnit();
        enemyUnits = new ArrayList<Unit>();
        enemyUnits.addAll(enemyPlayer.getUnits());

    //--MOVE
        Point move = makeMove();


        if(move == null)
            return new NullAction(myUnit);
        //MOVE IN BOARD
        if (! myBoard.moveUnitSafe(myUnit, move))
            throw new RuntimeException("SOMETHING WRONG, CANNOT MOVE unit "+myUnit.unitCode()+" IN GROUP. "+
                    "("+myUnit.coord().x+","+myUnit.coord().y+")"+
                    "-->"+ "("+move.x +","+ move.y+")");

    //--BUILD
        Point build = makeBuild();

        if(build == null)
            return new NullAction(myUnit);

        return new actionSet(player.getFirstUnit(), move, build);
    }

    private Point makeMove() throws Exception {
        ASPInputProgram moveProgram = new ASPInputProgram();
        moveProgram.addFilesPath(Settings.PATH_ENCOD_GROUP1+ "/move.asp");
        myHandler.setEncoding(moveProgram);

    //--REFRESH FACTS
        refreshFacts();

    //--CAN'T MOVE
        //moveCell(X,Y). --> cell where I can move unit
        ArrayList<Point> moveableArea = myBoard.moveableArea(myUnit);
        if (moveableArea.isEmpty()) {
            return null;
        }

    //--CAN MOVE
        //moveCell(X,Y,H)
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

    private Point makeBuild() throws Exception {
        ASPInputProgram buildProgram = new ASPInputProgram();
        buildProgram.addFilesPath(Settings.PATH_ENCOD_GROUP1+ "/build.asp");
        myHandler.setEncoding(buildProgram);

    //--REFRESH FACTS
        refreshFacts();

    //--CAN'T BUILD
        //moveCell(X,Y). --> cell where I can move unit
        ArrayList<Point> buildableArea = myBoard.buildableArea(myUnit);
        if (buildableArea.isEmpty()) {
            return null;
        }

    //--CAN BUILD

        //buildCell(X,Y,H)
        for (Point cell : buildableArea)
            myHandler.addFactAsString("buildCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell)+ ")");





        myHandler.startSync();
//        System.out.println(myHandler.getFactsString());
        //ADDING FACTS
        for (Object atom : myHandler.getOptimalAnswerSets().getFirst().getAtoms()) {
            if (atom instanceof buildIn){
                Point buildIn = new Point(((buildIn) atom).getX(), ((buildIn) atom).getY());
                return buildIn;
            }
        }

        throw new RuntimeException("Something wrong in makeBuild");
    }


//--UTILITY-------------------------------------------------------------------------------------------------------------

    /**
     * Refresh facts in myHandler without calling PlayerAi.refreshGridstate().
     */
    private void refreshFacts() throws Exception {
        ASPInputProgram myGridState = new ASPInputProgram();
        int [][] grid = myBoard.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                myGridState.addObjectInput((new cell(i, j, grid[i][j], myBoard.playerCodeAt(i, j))));
            }
        }

        //cell(X,Y,H,P).
        myHandler.setFactProgram(myGridState);

        //myPlayer(p)
        myHandler.addFactAsString("player("+myPlayer.getPlayerCode()+")");

        //unit(X,Y,H,U,P)
        myHandler.addFactAsObject(new unitASP(myUnit, myBoard.heightAt(myUnit.coord())));
        enemyUnits.forEach(unit-> {
            try {
                myHandler.addFactAsObject(new unitASP(unit, myBoard.heightAt(unit.coord())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //choosedUnit(U)
        myHandler.addFactAsString("choosedUnit("+myUnit.unitCode()+")");

        //enemyMoveCell(X,Y,H,U).
        for (Unit enemyUnit : enemyPlayer.getUnits() )
            for (Point cell : myBoard.moveableArea(enemyUnit))
                myHandler.addFactAsString("enemyMoveCell("+ cell.x+","+cell.y+","+ myBoard.heightAt(cell)+","+enemyUnit.unitCode()+")");
    }

//--TEST----------------------------------------------------------------------------------------------------------------
    //TODO: ELIMINARE DOPO FASE SVILUPPO
    //TEST IF UNIT DOESN'T MOVE TO A CELL WITH HEIGHT 3
    private void testOptimalMove(Point move, int[][] grid, ArrayList<Point> moveAbleArea){
//      % prefer moving to an height 3 cell
        if (grid[move.x][move.y] != 3 ){
            for (Point cell : moveAbleArea)
                if (grid[cell.x][cell.y] == 3 && ! move.equals(cell))
                    throw new RuntimeException("TEST FAILED, Move Not Optimal. Group1 move to ("+move.x+","+move.y+")"+
                            " instead of ("+cell.x+","+cell.y+")");
        }

//      % prefer moving on higher cell
        for (Point cell : moveAbleArea)
            if (grid[move.x][move.y] < grid[cell.x][cell.y] && ! move.equals(cell))
                throw new RuntimeException("TEST FAILED, Move Not Optimal. Group1 move to ("+move.x+","+move.y+")"+
                        " instead of ("+cell.x+","+cell.y+")");

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
