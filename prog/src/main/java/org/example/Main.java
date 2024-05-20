package org.example;

import org.example.threads.PlayerThread;
import org.example.threads.Players;

import java.util.Scanner;


public class Main {
    public static Scanner sc = new Scanner(System.in);

    private static Matrix initGame(){
        Matrix gameBoard = new Matrix();
        System.out.print("Quanti giocatori? :");
        int numPlayer = sc.nextInt();

        for(int i = 0; i < numPlayer; i++){
            System.out.print("Cordinate "+(i+1)+" giocatore? <x,y>:");
            String coord = sc.next();
            System.out.print("Simbolo "+(i+1)+" giocatore? :");
            String symbol = sc.next();
            gameBoard.insertPlayer(coord,symbol);
        }
        return gameBoard;
        
    }

    private static void startGame(Matrix board){
        boolean repeatFlag = false;
        String moveCoord;
        String wallCoord;
        while(true){
            board.display();
            int currentTurn = board.getCurrentTurn();

            do {
                System.out.println("Giocatore "+currentTurn+" posizione muro? <x,y>");
                wallCoord = sc.next();
            } while(! board.placeWall(wallCoord));

            board.display();

            do {
                System.out.println("Giocatore "+currentTurn+" posizione movimento? <x,y>");
                moveCoord = sc.next();
            } while(! board.movePlayer(currentTurn, moveCoord));

            board.
                    nextTurn();
        }
    }

    /**
     * Start the game with player as threads
     */
    private static void startGameThreads(Matrix board){
    //--INIT THREADS
        Players.getInstance().initPlayerThread(board);

    //--START GAME
        System.out.println("Griglia iniziale:");
        board.display();
        Players.getInstance().startPlayerThread();

    }


    public static void main(String[] args) {
        System.out.println("Inserire 0 per eseguire il codice senza i thread, 1 per eseguire il codice con i thread");
        int choice = sc.nextInt();

        switch (choice) {
            case 0:
                startGame(initGame());

            case 1:
                Matrix gameboard = new Matrix();
                gameboard.insertPlayer("0,0", "A");
                gameboard.insertPlayer("2,2", "B");
                startGameThreads(gameboard);

            default:
                System.out.println("Scelta non valida");
        }
    }
}
