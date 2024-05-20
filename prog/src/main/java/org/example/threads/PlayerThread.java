package org.example.threads;

import org.example.Matrix;

public class PlayerThread implements Runnable {
    private final Matrix board;
    private final int playerID;

    PlayerThread(Matrix board, int playerID) {
        this.board = board;
        this.playerID = playerID;
    }

//PROVA PER VEDERE SE FUNZIONA
    @Override
    public void run() {
        while (true) //TODO: add condition to stop the thread
            board.playTurn(playerID);
    }
}
