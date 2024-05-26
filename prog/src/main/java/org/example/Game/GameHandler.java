package org.example.Game;


import org.example.Game.embASP.PlayerAi;

import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Static class used to handle the game board.
 */
//TODO: cambiare sta classe in singleton
public class GameHandler {
    public static final int RUN_MODE_MANUAL = 0;
    public static final int RUN_MODE_AI = 1;

    private static final Scanner sc = new Scanner(System.in);
    private static Board board ;

    private static final Player[] players = new Player[Board.N_PLAYERS];

//--RUN METHODS--------------------------------------------------------------------------------------------------------
    public static Board getBoard(){
        return board;
    }

    //TODO: cambiare sti parametri
    //!!!Actually works only for single unit per player
    public static void runAI(String folderPath1, String folderPath2, int groupID1, int groupID2){
        char [] symbols = Input.startInput();

        players[0] = new PlayerAi(symbols[0], 0,folderPath1, groupID1);
        players[1] = new PlayerAi(symbols[1], 1,folderPath2, groupID2);
        initGame();

        System.out.println("---AI MODE---");
        board.display();

    //--THREADS
        ExecutorService executorService=Executors.newFixedThreadPool(2);
        try {
            while (true){
                for (Player player : players) {
                    actionSet action = executorService.submit((PlayerAi) player).get();
                    playTurn(action);


                    if (board.isTerminated()){
                        sc.close();
                        return;
                    }

                    Thread.sleep(500);

                }

            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

    //!!!Actually works only for single unit per player
    public static void runManual(){
        char [] symbols = Input.startInput();
        players[0]= new Player(symbols[0], 0);
        players[1]= new Player(symbols[1], 1);

        initGame();

        System.out.println("---MANUAL MODE---");
        board.display();

    //--1 UNIT PER PLAYER
        if (Board.UNIT_PER_PLAYER ==1){
            while (true){
                for (Player player : players) {
                //--MOVE
                    board.moveUnitSafe(player.getFirstUnit().unitCode(), Input.moveManual(player));

                //--DISPLAY
                    board.display();

                //--BUILD
                    board.buildFloor(player.getFirstUnit().unitCode(), Input.buildManual(player));

                //--DISPLAY
                    board.display();

                //--EXIT
                    if (board.isTerminated()){
                        sc.close();
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


    private static void playTurn(actionSet action){
//        System.out.println(action.toString());
        board.moveUnitSafe(action.unit().unitCode(), action.move());
        board.display();
        board.buildFloor(action.unit().unitCode(),action.build());
        board.display();

    }
//--TEST METHODS--------------------------------------------------------------------------------------------------------
//    private boolean compareGrids() throws ObjectNotValidException, IllegalAnnotationException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        String [][] embGrid = new String[Board.GRID_SIZE][Board.GRID_SIZE];
//        for (String[] strings : embGrid) {
//            Arrays.fill(strings, Board.EMPTY_CELL_VAL);
//        }
//
//    //--REFRESH EMBASP GRID
//
//        handlerFacts.startSync();
//        AnswerSets answerSets =  (AnswerSets) handlerFacts.getOutput();
//        for (AnswerSet as : answerSets.getAnswersets()) {
//            for (Object obj : as.getAtoms()) {
//                if (obj instanceof player p) {
//                    embGrid[p.getX()][p.getY()] = board.getPlayerSymbol(p.getPlayerCode());
//                }
//                else if (obj instanceof floor f) {
//                    embGrid[f.getX()][f.getY()] = String.valueOf(f.getHeight());
//                }
//
//            }
//        }
//
//
//        //--COMPARE
//        String[][] grid = board.getGrid();
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid[i].length; j++) {
//                if (! Objects.equals(grid[i][j], embGrid[i][j])) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }




}
