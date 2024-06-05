package org.example.Game.gameManager;


import org.example.Game.mode.Player;
import org.example.Game.mode.ai.PlayerAi;
import org.example.Game.mode.ai.actionSet;
import org.example.Game.mode.manual.PlayerManual;
import org.example.embAsp.cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Static class used to handle the game board.
 */
//TODO: cambiare sta classe in singleton
public class GameHandler {
    public static final int RUN_MODE_MANUAL = 0;
    public static final int RUN_MODE_AI = 1;

    static Board board ;

    private static Player[] players ;

//--RUN METHODS--------------------------------------------------------------------------------------------------------
    public static Board getBoard(){
        return board;
    }

    //TODO: cambiare sti parametri
    //!!!Actually works only for single unit per player
    public static void runAI(int groupID1, int groupID2) throws Exception {
    //--CREATE PLAYERS
        char [] symbols = Input.startInput();
        players = new PlayerAi[Board.N_PLAYERS];
        players[0] = new PlayerAi(symbols[0], 0, groupID1);
        players[1] = new PlayerAi(symbols[1], 1, groupID2);
        initGame();
        refreshGridState();

    //--JUST AESTHETIC
        String aiMode = "AI  MODE";

        for (int i = 0; i < 1; i++) {
            System.out.println();
            for (int j = 0; j < 50; j++) {
                System.out.print("*");
            }
        }

        System.out.println();

        for (int i = 0; i < 25 - aiMode.length()/2; i++) {
            System.out.print("*");
        }
        System.out.print(aiMode);
        for (int i = 0; i < 25 - aiMode.length()/2; i++) {
            System.out.print("*");
        }
        for (int i = 0; i < 1; i++) {
            System.out.println();
            for (int j = 0; j < 50; j++) {
                System.out.print("*");
            }
        }

        System.out.print("\n\nSPAWN UNITS");
        board.display();

    //--THREADS
        ExecutorService executorService=Executors.newFixedThreadPool(2);

        try {
    //--GAMELOOP
            while (! board.Win()){
                for (Player p : players) {
                    actionSet action = executorService.submit((PlayerAi) p).get();
                    playTurn(action);

//                    Thread.sleep(500);

                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

    //!!!Actually works only for single unit per player
    public static void runManual(){
        char [] symbols = Input.startInput();
        players = new Player[Board.N_PLAYERS];
        players[0]= new PlayerManual(symbols[0], 0);
        players[1]= new PlayerManual(symbols[1], 1);

        initGame();

        System.out.println("---MANUAL MODE---");
        board.display();

    //--1 UNIT PER PLAYER
        if (Board.UNIT_PER_PLAYER ==1){
            while (true){
                for ( Player p : players) {
                //--MOVE
                    board.moveUnitSafe(p.getFirstUnit(), Input.moveManual((PlayerManual) p));
                //--DISPLAY
                    board.display();

                //--BUILD
                    board.buildFloor(p.getFirstUnit(), Input.buildManual((PlayerManual) p));

                //--DISPLAY
                    board.display();

                //--EXIT
                    if (board.Win()){
                        return;
                    }
                }
            }
        }

        //--MULTIPLE UNITS PER PLAYER
        else {

        }
    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------

    private static void initGame(){
    //--ADD PLAYERS
        board = new Board(players);

    //--RANDOM SET UNIT POSITION
        Random rand = new Random();

        for (int p = 0; p < Board.N_PLAYERS; p++) {
            for (int u = 0; u < Board.UNIT_PER_PLAYER  ; u++) {

                Point coord = new Point(rand.nextInt(Board.ROW_SIZE), rand.nextInt(Board.COL_SIZE));
                while (board.isOccupied(coord)){
                      coord = new Point(rand.nextInt(Board.ROW_SIZE), rand.nextInt(Board.COL_SIZE));
                }
                board.addUnit(p, coord) ;



            }
        }
    }


    private static synchronized void playTurn(actionSet action) throws Exception {
    //--WIN CONDITION -> can not make action
        if (action.isNullAction()){
            board.setWin();
            System.out.println("\nPLAYER "+ action.unit().player().getSymbol() + "LOSE. CAN'T MAKE ANY ACTION!");
            return;
        }

        System.out.print( action.display());
    //--MOVE AND BUILD
        //TODO: RIMUOVERE IF AL TERMINE DELLA FASE DI SVILUPPO O COMUNQUE NON SERVONO PIU
        if(! board.moveUnitSafe(action.unit(), action.move()))
            //LA TUA AI SI E' FATTA UNO SPINIELLO BRO
            throw new RuntimeException("Invalid move "+ action.move() + " for unit "+ action.unit().unitCode());

        if (! board.buildFloor(action.unit(),action.build()))
            //LA TUA AI SI E' FATTA PIU' DI UNO SPINIELLO BRO
            throw new RuntimeException("Invalid build "+ action.build() + " for unit "+ action.unit().unitCode());

        board.display();

    //--WIN CONDITION -> unit on height 3
        if (board.Win()){
            for (int i = 0; i < 50; i++) {
                System.out.print("*");
            }
            System.out.println("\nPLAYER "+ action.unit().player().getSymbol() + " WINS!");
            return;
        }

    //--REFRESH GRID STATE
        refreshGridState();
    }

    private static void refreshGridState() throws Exception {
        ArrayList<cell> cells = new ArrayList<>();
        int [][] grid = board.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                cells.add((new cell(i, j, grid[i][j], board.playerCodeAt(i, j))));
            }
        }
        PlayerAi.refreshGridState(cells);

    }
//--TEST METHODS--------------------------------------------------------------------------------------------------------

    /**
     * Lancia un loop di partite che termina solo in caso di Exception.
     * @throws Exception
     */

    //TODO: rimuovere dopo sviluppo
    public static void testAiBruteForce() throws Exception {
        char [] symbols = {'a','b'};
        players = new PlayerAi[Board.N_PLAYERS];
        players[0] = new PlayerAi(symbols[0], 0, PlayerAi.GROUP_1);
        players[1] = new PlayerAi(symbols[1], 1 , PlayerAi.GROUP_1);
        initGame();
        refreshGridState();


        System.out.print("\n\nSPAWN UNITS");
        board.display();

    //--THREADS
        ExecutorService executorService=Executors.newFixedThreadPool(2);
        try {
            while (true){
                for (Player p : players) {
                    actionSet action = executorService.submit((PlayerAi) p).get();
                    playTurn(action);


                    if (board.Win()){
                        testAiBruteForce();
                        return;
                    }

//                    Thread.sleep(500);

                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }




}
