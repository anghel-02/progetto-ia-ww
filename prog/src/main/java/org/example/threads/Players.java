package org.example.threads;


import org.example.Matrix;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Singleton class used to manage the players as threads
 */
public class Players {
    private static Players instance = null;

    private final ArrayList<PlayerThread> players;

    private ExecutorService executor;

    private Players() {
        players = new ArrayList<PlayerThread>(2);
    }

    public static Players getInstance() {
        if (instance == null) {
            instance = new Players();
        }
        return instance;
    }



//--THREAD METHODS-------------------------------------------------------------------------------------------------------

    /**
     * Initialize the player threads
     * @param board the game board
     */
    public void initPlayerThread(Matrix board) {
    //--CHECK
        if (board == null || board.getPlayerCounter() == 0){
            System.out.println("No players to start");
            return;
        }

    //--SET NUMBER OF PLAYERS
        for (int i = 0; i < board.getPlayerCounter(); i++) {
            players.add(new PlayerThread(board, i+1));//io so che il primo giocatore ha playerCode = 1
        }

    //--INIT THREADS
        executor = Executors.newFixedThreadPool(players.size());

    }

    /**
     * Start the player threads
     */
    public void startPlayerThread(){
        for (PlayerThread player : players) {
            executor.execute(player);
        }
    }

    /**
     * Stop the player threads
     */
    public void stopPlayerThread(){
        executor.shutdownNow();
    }




}
