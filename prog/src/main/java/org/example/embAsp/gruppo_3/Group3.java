package org.example.embAsp.gruppo_3;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.example.Game.gameManager.Board;
import org.example.Game.gameManager.GameHandler;
import org.example.Game.mode.Unit;
import org.example.Game.mode.ai.NullAction;
import org.example.Game.mode.ai.PlayerAi;
import org.example.Game.mode.ai.actionSet;
import org.example.Settings;
import org.example.embAsp.*;

import java.awt.*;
import java.util.ArrayList;


public class Group3 implements Group {
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
        myHandler.addFactAsObject(new myUnit3(myUnit, myBoard.heightAt(myUnit.coord())));

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
        ASPInputProgram moveProgram = new ASPInputProgram();
        moveProgram.addFilesPath(Settings.PATH_ENCOD_GROUP3+ "/move.asp");
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
                return moveIn;
            }
        }

        throw new RuntimeException("Something wrong in makeMove");
    }

    private Point makeBuild() throws Exception {
//        myHandler.setEncoding(myPlayer.getEncodings().get(1)); linux
        ASPInputProgram buildProgram = new ASPInputProgram();

        buildProgram.addFilesPath(Settings.PATH_ENCOD_GROUP3+ "/build.asp");
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
        myHandler.addFactAsObject(new myUnit3(myUnit, myBoard.heightAt(myUnit.coord())));
    }


    //--
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group3 group3 = (Group3) o;
        return myBoard.equals(group3.myBoard) && myPlayer.equals(group3.myPlayer) && myHandler.equals(group3.myHandler) && myUnit.equals(group3.myUnit);
    }

}