package org.example.threads;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import org.example.Matrix;
import org.example.Settings;
import org.example.embAsp.MyHandler;
import org.example.embAsp.floor;
import org.example.embAsp.player;


import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Singleton class used to manage the game as a thread
 */
public class GameRunner {
    private static GameRunner instance = null;

    private final Matrix board;
    private final int playerCount;

    private MyHandler handlerFacts;

//--CONSTRUCTOR---------------------------------------------------------------------------------------------------------
    private GameRunner(Matrix board) {
        this.board = board;
        this.playerCount = board.getPlayerCounter();

    //--INITIALIZE EMBASP HANDLER
        initEmbASP();
    }

    public static GameRunner getInstance() {
        if (instance == null) {
            instance = new GameRunner(Matrix.getInstance());
        }
        return instance;
    }

//--EMBASP METHODS------------------------------------------------------------------------------------------------------
    private void initEmbASP(){
        this.handlerFacts = new MyHandler();
//        handlerFacts.addEncoding("encodings/GridState.asp");
        try {
            handlerFacts.mapToEmb(player.class);
            handlerFacts.mapToEmb(floor.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e) {
            throw new RuntimeException(e);
        }
    }
//--TEST METHODS--------------------------------------------------------------------------------------------------------
    private boolean compareGrids() throws ObjectNotValidException, IllegalAnnotationException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        String [][] embGrid = new String[Matrix.GRID_SIZE][Matrix.GRID_SIZE];
        for (String[] strings : embGrid) {
            Arrays.fill(strings, Matrix.EMPTY_CELL_VAL);
        }

    //--REFRESH EMBASP GRID

        handlerFacts.startSync();
        AnswerSets answerSets =  (AnswerSets) handlerFacts.getOutput();
        for (AnswerSet as : answerSets.getAnswersets()) {
            for (Object obj : as.getAtoms()) {
                if (obj instanceof player p) {
                    embGrid[p.getX()][p.getY()] = board.getPlayerSymbol(p.getPlayerCode());
                }
                else if (obj instanceof floor f) {
                    embGrid[f.getX()][f.getY()] = String.valueOf(f.getHeight());
                }

            }
        }


        //--COMPARE
        String[][] grid = board.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (! Objects.equals(grid[i][j], embGrid[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }



//--THREAD METHODS------------------------------------------------------------------------------------------------------
    public void run() {
        while (true) {
            for (int i = 0; i < playerCount; i++) {

                //--REFRESH BOARD STATE IN GridState.asp
                //provvissorio--Vincenzo Sacco
                try {
                    //--CLEAR OLD FACTS
                    handlerFacts.clearAll();

                    //--ADD NEW FACTS
                    handlerFacts.addEncoding(Settings.PATH_TO_GridState);
                    handlerFacts.addFactAsString("player(" + board.getPlayerPosition(1) + ",1)");
                    handlerFacts.addFactAsString("player(" + board.getPlayerPosition(2) + ",2)");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                //!!RIMUOVERE DOPO FASE DI SVILUPPO!!--CHECK IF THE GRID FOR AI IS EQUAL TO THE GRID FOR THE GAME

                try {
                    if (!compareGrids()) {
                        throw new RuntimeException("\nThe grid for AI is not equal to the grid for the game");
                    }
                } catch (ObjectNotValidException | IllegalAnnotationException | InvocationTargetException |
                         IllegalAccessException | NoSuchMethodException | InstantiationException e) {
                    throw new RuntimeException(e);
                }


                //--PLAY TURNS

                board.playTurn(i + 1);
                if (board.isTerminated())
                    return;
            }
        }
    }
}
