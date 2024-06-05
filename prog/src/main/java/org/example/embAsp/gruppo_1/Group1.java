package org.example.embAsp.gruppo_1;

import org.example.Game.gameManager.Board;
import org.example.Game.gameManager.GameHandler;
import org.example.Game.mode.Player;
import org.example.Game.mode.ai.PlayerAi;
import org.example.Game.mode.ai.actionSet;
import org.example.Game.mode.Unit;
import org.example.embAsp.Group;
import org.example.embAsp.WondevWomanHandler;
import org.example.embAsp.moveIn;
import org.example.embAsp.buildIn;

import java.awt.*;
import java.util.ArrayList;


public class Group1 implements Group {
    private static Board myBoard;
    private static PlayerAi myPlayer;
    private static WondevWomanHandler myHandler;
    private static Unit myUnit;

    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = Board.copyOf(GameHandler.getBoard());
        myPlayer= new PlayerAi(player);
        myHandler = myPlayer.getHandler();

    //--CHOSE UNIT
        myUnit = myPlayer.getFirstUnit();

    //--MOVE
        Point move = makeMove();
        if(move == null)
            throw new RuntimeException("IMPLEMENTA ");

        myBoard.moveUnitSafe(myUnit, move);

    //--BUILD
        Point build = makeBuild();

        if(build == null)
            throw new RuntimeException("IMPLEMENTA ");

        return new actionSet(player.getFirstUnit(), move, build);
    }

    private Point makeMove() throws Exception {
    //TODO: CAMBIARE METODO SCELTA ENCODING, MOLTO VULNERBILE USANDO INDICI
        myPlayer.chooseEncoding(1);


    //--CAN'T MOVE
        //moveCell(X,Y). --> cell where I can move unit
        ArrayList<Point> moveableArea = myBoard.moveableArea(myUnit);
        if (moveableArea.isEmpty()) {
            return null;
        }

    //--CAN MOVE
        for (Point cell : moveableArea)
            myHandler.addFactAsString("moveCell(" + cell.x + "," + cell.y + ")");

        myHandler.startSync();
        //ADDING FACTS
        for (Object atom : myHandler.getAnswerSetsList().getFirst().getAtoms()) {
            if (atom instanceof moveIn)
                return new Point(((moveIn) atom).getX(), ((moveIn) atom).getY());
        }

        return null;
    }

    //TODO: ATTENZIONE I FATTI AGGIUNTI DA MOVE NON VENGONO CANCELLATI, MA PER ORA NON VANNO IN CONFLITTO
    private Point makeBuild() throws Exception {
        myPlayer.chooseEncoding(0);

    //--CAN'T BUILD
        //moveCell(X,Y). --> cell where I can move unit
        ArrayList<Point> buildableArea = myBoard.buildableArea(myUnit);
        if (buildableArea.isEmpty()) {
            return null;
        }

    //--CAN BUILD
        for (Point cell : buildableArea)
            myHandler.addFactAsString("buildCell(" + cell.x + "," + cell.y + ")");

        myHandler.startSync();

        //ADDING FACTS
        for (Object atom : myHandler.getAnswerSetsList().getFirst().getAtoms()) {
            if (atom instanceof buildIn)
                return new Point(((buildIn) atom).getX(), ((buildIn) atom).getY());
        }

        return null;
    }

}
