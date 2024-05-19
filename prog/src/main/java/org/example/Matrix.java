package org.example;

import java.util.HashMap;

public class Matrix {
    private static final int GRID_SIZE = 5;
    private String[][] grid;
    private int[][] wallGrid;
    private int playerCounter = 0;
    private int currentTurn = 1;

    HashMap<Integer,String> posizioni = new HashMap<Integer,String>();
    HashMap<Integer,String> simboli = new HashMap<Integer,String>();

    public Matrix() {
        grid = new String[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = " ";
            }
        }
    }

    private int[] coordConvert(String coord) {
        int[] c = new int[2];
        c[0] = Integer.parseInt(String.valueOf(coord.charAt(0)));
        c[1] = Integer.parseInt(String.valueOf(coord.charAt(2)));
        return c;
    }

    public void display() {
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print("|");
            for (int j = 0; j < GRID_SIZE; j++)
                System.out.print(grid[i][j] + "|");
            System.out.println();
        }
    }

    public void insertPlayer(String coord, String symbol) {
        int[] nCoord = coordConvert(coord);
        int x = nCoord[0];
        int y = nCoord[1];

        grid[x][y] = symbol;
        this.playerCounter++;
        posizioni.put(playerCounter,coord);
        simboli.put(playerCounter, symbol);
    }

    public int getCurrentTurn() {return currentTurn; };

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
