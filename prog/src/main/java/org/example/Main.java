package org.example;

import org.example.embAsp.MyHandler;
import org.example.threads.GameRunner;


import java.util.Scanner;


public class Main {
    public static Scanner sc = new Scanner(System.in);

    private static void initGame(){
        Matrix gameBoard = Matrix.getInstance();
        System.out.print("Quanti giocatori? :");
        int numPlayer = sc.nextInt();

        for(int i = 0; i < numPlayer; i++){
            System.out.print("Cordinate "+(i+1)+" giocatore? <x,y>:");
            String coord = sc.next();
            System.out.print("Simbolo "+(i+1)+" giocatore? :");
            String symbol = sc.next();
            gameBoard.insertPlayer(coord,symbol);
        }

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
     * Start the game with player managed by AI
     */
    private static void startGameAI(){
        GameRunner.getInstance().run();
    }


    public static void main(String[] args) {
    //--SET PATH TO DLV2
        MyHandler.setRelPathToDLV2(Settings.PATH_TO_DLV2);

    //--
        System.out.println("Inserire 0 per eseguire il codice in manuale, 1 per eseguire il codice con AI");
        int choice = sc.nextInt();

        switch (choice) {
            case 0:
                initGame();
                startGame(Matrix.getInstance());
                break;
            case 1:
                Matrix gameBoard = Matrix.getInstance();
                gameBoard.insertPlayer("0,0", "A");
                gameBoard.insertPlayer("2,2", "B");
                startGameAI();

                sc.close(); //close the scanner
                break;
            default:
                System.out.println("Scelta non valida");
        }
    }
}
