package org.example;

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

            board.nextTurn();
        }
    }

    public static void main(String[] args) {
        Matrix gameboard = initGame();
        startGame(gameboard);

    }
}
