package org.example.embAsp.gruppo_1;

import org.example.Game.gameManager.Board;
import org.example.Game.gameManager.GameHandler;
import org.example.Game.mode.Player;
import org.example.Game.mode.Unit;
import org.example.Game.mode.ai.NullAction;
import org.example.Game.mode.ai.PlayerAi;
import org.example.Game.mode.ai.actionSet;
import org.example.embAsp.Group;
import org.example.embAsp.WondevWomanHandler;
import org.example.embAsp.buildIn;
import org.example.embAsp.moveIn;

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
     * @return
     * @throws Exception
     */
    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = GameHandler.getBoard().copy();
        for (Player p : myBoard.getPlayers())
            if (player.getPlayerCode() == p.getPlayerCode())
                myPlayer = (PlayerAi) p;

        myHandler = myPlayer.getHandler();

    //--CHOSE UNIT
        myUnit = myPlayer.getFirstUnit();

    //--MOVE
        Point move = makeMove();
        if(move == null)
            return new NullAction(myUnit);

        if (! myBoard.moveUnitSafe(myUnit, move))
            throw new RuntimeException("SOMETHING WRONG, CANNOT MOVE IN GROUP");

    //--BUILD
        Point build = makeBuild();

        if(build == null)
            return new NullAction(myUnit);

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

        throw new RuntimeException("Something wrong in makeMove");
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

        throw new RuntimeException("Something wrong in makeBuild");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group1 group1 = (Group1) o;
        return myBoard.equals(group1.myBoard) && myPlayer.equals(group1.myPlayer) && myHandler.equals(group1.myHandler) && myUnit.equals(group1.myUnit);
    }

}
