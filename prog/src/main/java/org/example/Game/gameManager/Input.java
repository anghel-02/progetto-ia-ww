package org.example.Game.gameManager;

import org.example.Game.mode.manual.PlayerManual;

import java.awt.*;
import java.util.Scanner;


class Input {
    private static final Scanner sc = new Scanner(System.in);
    private final static String ACTION_REGEX = "[0-"+(Board.ROW_SIZE-1)+"],[0-"+(Board.COL_SIZE-1)+"]";

//--START GAME----------------------------------------------------------------------------------------------------------
     static char[] startInput(){
        char[] playersSymbols = new char[Board.N_PLAYERS];

        for (int i = 0; i < Board.N_PLAYERS; i++) {
            System.out.print("Player " + (i+1)+ " symbol: ");
            playersSymbols[i] = sc.next().charAt(0);
        }
        return playersSymbols;
}


//--MANUAL INPUT--------------------------------------------------------------------------------------------------------

    //!!! Actually works only for single unit per player
    static Point moveManual(PlayerManual player) {
        String toPrint = "Player " + player.getSymbol() + " moves to (x,y): ";
    //--INPUT
         System.out.print(toPrint);

        String moveStr = sc.next();
        while (!moveStr.matches(ACTION_REGEX)) {
            System.out.println("Invalid input");
            System.out.print(toPrint);
            moveStr = sc.next();
        }

    //--CHECK
        Point move;
        int x = Integer.parseInt(moveStr.substring(0,1));
        int y = Integer.parseInt(moveStr.substring(2));
        move = new Point( x,y);
        if (! GameHandler.board.canMove(player.getFirstUnit(), move))
            return moveManual(player);


        return move;
    }


    //!!! Actually works only for single unit per player
    static Point buildManual(PlayerManual player) {
        String toPrint = "Player "+player.getSymbol()+ " builds at (x,y): ";

    //--INPUT
        System.out.print(toPrint);

        String buildStr = sc.next();
        while (! buildStr.matches(ACTION_REGEX)){
            System.out.println("Invalid input");
            System.out.print(toPrint);
            buildStr = sc.next();
        }

    //--CHECK
         Point build;
         int x = Integer.parseInt(buildStr.substring(0,1));
         int y = Integer.parseInt(buildStr.substring(2));
         build = new Point(x,y);

         if (! GameHandler.board.canBuild(player.getFirstUnit(), build))
             return buildManual(player);

         return build;

    }

}
