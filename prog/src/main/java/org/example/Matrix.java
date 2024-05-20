package org.example;

import org.example.threads.Players;

import java.util.HashMap;

public class Matrix {
    private static final int GRID_SIZE = 5;
    private String[][] grid;
    private int[][] wallGrid;
    private int playerCounter = 0;
    private int currentTurn = 1;


    HashMap<Integer,String> posizioni = new HashMap<Integer,String>();
    HashMap<Integer,String> simboli = new HashMap<Integer,String>();

    private final Object lock;

    public Matrix() {
        grid = new String[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = " ";
            }
        }

        lock = new Object();
    }

//--GETTERS & SETTERS---------------------------------------------------------------------------------------------------

    public int getPlayerCounter() {
        return playerCounter;
    }

    /**
     * Get the position of the player in the grid.
     *
     * @param playerCode the key of the player in {@code HashMap posizioni}
     * @return the position of the player in the grid.
     */
    public String getPlayerPosition(int playerCode) {
        return posizioni.get(playerCode);
    }

    public int getCurrentTurn() {return currentTurn; };

//--UTILITY-------------------------------------------------------------------------------------------------------------

    private int[] coordConvert(String coord) {
        int[] c = new int[2];
        c[0] = Integer.parseInt(String.valueOf(coord.charAt(0)));
        c[1] = Integer.parseInt(String.valueOf(coord.charAt(2)));
        return c;
    }

//--THREADS-------------------------------------------------------------------------------------------------------------
    /**
     * This method must be called by PlayerThread to play his turn
     */
    public void playTurn(int playerCode){

    //--WAIT TO ACQUIRE LOCK
        synchronized (lock) {
            while (currentTurn != playerCode ) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

    //--ACTUAL POSITION
        String pos = getPlayerPosition(playerCode);

    //--WALL
        //PER PROVA piazza muro a destra
        String wallPos = pos.charAt(0) + "," + (Integer.parseInt(String.valueOf(pos.charAt(2))) + 1);
        System.out.println("Giocatore" + playerCode + " posiziona muro in " + wallPos);
        placeWall(wallPos);

        display();

    //--MOVE
        //PER PROVA muove il giocatore in basso , termina il thread se arriva alla fine
        String movePos = (Integer.parseInt(String.valueOf(pos.charAt(0))) + 1) + "," + pos.charAt(2);
        System.out.println("Giocatore" + playerCode + " si muove in " + movePos);

        // PROVISSORIO
        if (! movePlayer(playerCode, movePos)){
            System.out.println("TERMINA IL THREAD (provissorio)");
            Players.getInstance().stopPlayerThread();
        }
        //


        display();

    //--NEXT TURN
        nextTurn();

    //--RELEASE LOCK
        lock.notify(); //ATTENZIONE , non ho ancora definito un ordine di accesso al lock nel caso ci fossero + di 2 giocatori.
        }
    }


//--GAME----------------------------------------------------------------------------------------------------------------

    public void display() {
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print("|");
            for (int j = 0; j < GRID_SIZE; j++)
                System.out.print(grid[i][j] + "|");
            System.out.println();
        }
    }

    /**
     * Insert a player in the grid.
     * @param coord
     * @param symbol
     * @return the playerCode
     */
    public int insertPlayer(String coord, String symbol) {
        int[] nCoord = coordConvert(coord);
        int x = nCoord[0];
        int y = nCoord[1];

        grid[x][y] = symbol;
        this.playerCounter++;
        posizioni.put(playerCounter,coord);
        simboli.put(playerCounter, symbol);

        return playerCounter; //potrebbe servirmi in futuro -> per ora non lo uso - Vincenzo Sacco
    }


    /**
     * Move the player in the grid.
     * @param playerCode
     * @param coord
     * @return true if the player moved, false otherwise
     */
    public boolean movePlayer(int playerCode, String coord) {
        // processo dell'input
        String symbol = simboli.get(playerCode);
        int[] playerCurrentPosition = coordConvert(posizioni.get(playerCode));
        int currentX = playerCurrentPosition[0];
        int currentY = playerCurrentPosition[1];

        int[] nCoord = coordConvert(coord);
        int nextX = nCoord[0];
        int nextY = nCoord[1];

        String newCasella = grid[nextX][nextY];
        String oldCasella = grid[currentX][currentY];


        //gestione movimento troppo lungo
        if(Math.pow(nextY-currentY, 2)>1 || Math.pow(nextX-currentX, 2)>1){
            System.out.println("non puoi, troppo lontano");
            return false;
        }

        if(newCasella.equals("X")){ //movimento su muro pieno
            System.out.println("movimento muro pieno, non puoi");
            return false;
        }

        if(newCasella.equals(" ")){ //movimento verso cella vuota
            System.out.println("movimento verso cella vuota");
            grid[nextX][nextY] = symbol;
            if(!oldCasella.equals(symbol))
                grid[currentX][currentY] = String.valueOf(grid[currentX][currentY].charAt(0));
            else grid[currentX][currentY]=" ";

            posizioni.replace(playerCode, String.valueOf(nextX)+","+String.valueOf(nextY));
            return true;
        } else if(newCasella.matches(".,.")) { //movimento spinta su muro
            System.out.println("movimento spinta su muro");
            //ricerca player spinto
            int playerSpinto = -1;
            for (HashMap.Entry<Integer, String> set : posizioni.entrySet()){
                if(set.getValue().equals(coord)) {
                    playerSpinto = set.getKey();
                }
            }
            System.out.println(playerCode+" spinge "+ playerSpinto);
            int spintaX = nextX+(nextX-currentX);
            int spintaY = nextY+(nextY-currentY);

            //se spingi verso i bordi devi ripetere
            if((spintaY>4 && spintaY<0) || (spintaX>4 && spintaX<0) || grid[spintaX][spintaY].equals("X"))
                return false;

            String coordSpinta = String.valueOf(spintaX)+","+String.valueOf(spintaY);
            System.out.println("coordinata spinta = " + coordSpinta);
            posizioni.replace(playerSpinto, coordSpinta);
            grid[nextX][nextY] = String.valueOf(grid[nextX][nextY].charAt(0));

            if(grid[spintaX][spintaY].matches("[0-9]*"))
                grid[spintaX][spintaY] = grid[spintaX][spintaY]+","+playerSpinto;
            else grid[spintaX][spintaY] = simboli.get(playerSpinto);

            return true;
        } else if(newCasella.matches("[0-9]*")){ //movimento salire su un muro
            System.out.println("movimento salgo su muro");
            grid[nextX][nextY]=grid[nextX][nextY]+","+symbol;

            if(!oldCasella.equals(symbol))
                grid[currentX][currentY] = String.valueOf(grid[currentX][currentY].charAt(0));
            else grid[currentX][currentY]=" ";

            posizioni.replace(playerCode, String.valueOf(nextX)+","+String.valueOf(nextY));
            return true;
        } else {
            System.out.println("spinta");
            //ricerca player spinto
            int playerSpinto = -1;
            for (HashMap.Entry<Integer, String> set : posizioni.entrySet()){
                if(set.getValue().equals(coord)) {
                    playerSpinto = set.getKey();
                }
            }

            System.out.println(playerCode+" spinge "+ playerSpinto);
            int spintaX = nextX+(nextX-currentX);
            int spintaY = nextY+(nextY-currentY);

            //se spingi verso i bordi devi ripetere
            if((spintaY>4 && spintaY<0) || (spintaX>4 && spintaX<0) || grid[spintaX][spintaY].equals("X"))
                return false;

            String coordSpinta = String.valueOf(spintaX)+","+String.valueOf(spintaY);
            System.out.println("coordinata spinta = " + coordSpinta);
            posizioni.replace(playerSpinto, coordSpinta);
            grid[nextX][nextY] = " ";

            if(grid[spintaX][spintaY].matches("[0-9]*"))
                grid[spintaX][spintaY] = grid[spintaX][spintaY]+","+playerSpinto;
            else grid[spintaX][spintaY] = simboli.get(playerSpinto);

            return true;
        }
    }

    public boolean placeWall(String coord) {
        int[] nCoord = coordConvert(coord);
        int x = nCoord[0];
        int y = nCoord[1];

        switch (grid[x][y]) {
            case " ":{
                grid[x][y]="1";
                return true;
            }
            case "1": {
                grid[x][y] = "2";
                return true;
            }
            case "2":{
                grid[x][y]="3";
                return true;
            }
            case "3":{
                grid[x][y]="X";
                return true;
            }
            case "X":{
                return false;
            }
            default: {
                System.out.println("non puoi");
                return false;
            }
        }
    }

    public void nextTurn() {
        this.currentTurn++;
        if (currentTurn>playerCounter) currentTurn = 1;


    }
}
