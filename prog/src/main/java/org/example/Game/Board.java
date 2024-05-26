package org.example.Game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that represents the game board.
 */
public class Board {
    private int NEXT_UNIT_CODE = 0; // need in case of multiple units per player



    public static final int N_PLAYERS = 2;
    public static final int UNIT_PER_PLAYER = 1;
    public static final int ROW_SIZE = 5;
    public static final int COL_SIZE = 5;

    public static final int FLOOR_HEIGHT_0= 0;
    public static final int FLOOR_HEIGHT_1= 1;
    public static final int FLOOR_HEIGHT_2= 2;
    public static final int FLOOR_HEIGHT_3= 3;
    public static final int FLOOR_HEIGHT_4= 4;
    public static final int FLOOR_START= FLOOR_HEIGHT_0;
    public static final int FLOOR_REMOVED= FLOOR_HEIGHT_4;

    private final int[][] grid;
    private final Player[] players;
    private final HashMap<Integer, Point> unitCoord = new HashMap<>(); // unitCode, coord

    private int CURRENT_TURN = 1;

    //    private final Object lock;
    private boolean isTerminated = false;

//--CONSTRUCTOR---------------------------------------------------------------------------------------------------------
     Board(Player [] players) {
        grid = new int[ROW_SIZE][COL_SIZE];
        this.players = new Player[N_PLAYERS];
        this.players[0] = players[0];
        this.players[1] = players[1];

        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < ROW_SIZE; j++) {
                grid[i][j] = FLOOR_START;
            }
        }

//        lock = new Object();
    }


//--GETTERS & SETTERS---------------------------------------------------------------------------------------------------

    /**
     * Get the grid of the game. <p>
     * The grid is a matrix of integers which represent floors height
     * @return
     */
    public int[][] getGrid() {
        return grid;
    }

    Player[] getPlayers() {
        return players;
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    /**
     * Check if the cell is occupied by a unit.
     * @param coord
     * @return
     */
    public boolean isOccupied(Point coord) {
        return isOccupied(coord.x,coord.y);
    }

    /**
     * Check if the cell is occupied by a unit.
     * @param x
     * @param y
     * @return {@code true} if the cell is occupied, {@code false} otherwise
     */
    public boolean isOccupied(int x, int y) {
        for (Point cell : unitCoord.values()){
            if (cell.x == x && cell.y == y)
                return true;
        }
        return false;
    }




//--GAME----------------------------------------------------------------------------------------------------------------
    private String [][] display = new String[ROW_SIZE][COL_SIZE];
    public void display() {
        System.out.println();

    //--REFRESH toDisplay
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                display[i][j] = "  " + grid[i][j] +"  ";
            }
        }

        for (Player p : players){
            for (Player.unit u : p.getUnits()){
                Point coord = u.coord();
                String floor = display[coord.x][coord.y].substring(0,3);
                display[coord.x][coord.y] = floor+ p.getSymbol() + u.unitCode();
            }
        }


    //--PRINT GRID
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++){
                System.out.print(display[i][j] );
            }
            System.out.println();
        }


    }

    public int addUnit(int playerCode, Point coord) {
        if(playerCode < 0 || playerCode > N_PLAYERS-1){
            throw new IllegalArgumentException("playerCode must be between 0 and " + (N_PLAYERS-1));
        }

        if (players[playerCode].getUnits().size() > UNIT_PER_PLAYER) {
            throw new IllegalStateException("Player already has the maximum number of units");
        }

        if (isOccupied(coord)){
            throw new IllegalArgumentException("Cell already occupied");
        }

    //--ADD UNIT TO player
        int unitCode = players[playerCode].addUnit(coord);

    //--ADD UNIT TO unitCoord
        unitCoord.put(unitCode ,coord) ;
        return unitCode;
    }

    public int addUnit(char symbol, int x, int y) {
         return addUnit(symbol, new Point(x,y));

    }

    public boolean canMove(int unitCode, Point coord){
//    A unit may move to any neighboring cell, including diagonals.
//    The unit may only move on the same level, step up one level or step down any number of levels.

//    After every movement, the unit must be able to build onto an adjacent cell of its new position.
//    This causes the cell in question to gain 1 unit of height.
//    If the height reaches level 4, the cell is considered removed from play.
//    If your unit moves onto a level 3 cell, you win the game.


        int currentX = unitCoord.get(unitCode).x;
        int currentY = unitCoord.get(unitCode).y;
        int toMoveX = coord.x;
        int toMoveY = coord.y;

    //--A unit cannot move to the same cell it is currently in.
        if (unitCoord.get(unitCode).equals(coord)){
            System.out.println("NOT VALID! same cell");
            return false;
        }

    //--A unit cannot move to a cell that is occupied by another unit.
        if (isOccupied(toMoveX,toMoveY)){
            System.out.println("NOT VALID! cell occupied");
            return false;
        }

    //--A unit may move to any neighboring cell, including diagonals.

        if( Math.abs(toMoveX-currentX)  > 1 || Math.abs(toMoveY-currentY)  > 1){
            System.out.println("NOT VALID! too far away");
            return false;
        }

    //--If the height reaches level 4, the cell is considered removed from play.
        if (grid[toMoveX][toMoveY] == FLOOR_REMOVED){
            System.out.println("NOT VALID! floor removed");
            return false;
        }
    //--The unit may only move on the same level, step up one level or step down any number of levels.
        if (grid[toMoveX][toMoveY] - grid[currentX][currentY] > 1){
            System.out.println("NOT VALID! floor too high");
            return false;
        }


        return true;

    }

    public boolean canMove(int unitCode, int x, int y){
        return canMove(unitCode, new Point(x,y));
    }



    /**
     * Move a unit to a new position. <p>
     * @param unitCode
     * @param coord
     * @return
     */
    public boolean moveUnitSafe(int unitCode, Point coord){
        if (canMove(unitCode,coord)){
            unitCoord.put(unitCode,coord); // if you put the same key, it will overwrite the value

            for (Player p : players)
                // if the unit is not found, it will return false and nothing will happen
                p.moveUnitSafe(unitCode,coord);

            return true;
        }
        return false;
    }

    /**
     * Move a unit to a new position. <p>
     * @param unitCode
     * @param x
     * @param y
     * @return
     */
    public boolean moveUnitSafe(int unitCode, int x, int y){
        return moveUnitSafe(unitCode, new Point(x,y));
    }


    public boolean canBuild(int unitCode, Point coord) {
        int currentX = unitCoord.get(unitCode).x;
        int currentY = unitCoord.get(unitCode).y;
        int toBuildX = coord.x;
        int toBuildY = coord.y;

        //--A unit cannot build to the same cell it is currently in.
        if (unitCoord.get(unitCode).equals(coord)){
            System.out.println("NOT VALID! same cell");
            return false;
        }

        //--A unit cannot build to a cell that is occupied by another unit.
        if (isOccupied(coord)){
            System.out.println("NOT VALID! cell occupied");
            return false;
        }

        //--A unit may build to any neighboring cell, including diagonals.
        if( toBuildX-currentX > 1 || toBuildY-currentY > 1){
            System.out.println("NOT VALID! too far away");
            return false;
        }

        //--If the height reaches level 4, the cell is considered removed from play.
        if (grid[toBuildX][toBuildY] == FLOOR_REMOVED){
            System.out.println("NOT VALID! floor removed");
            return false;
        }


        return true;
    }

    public boolean buildFloor(int unitCode,Point coord) {
        if (canBuild(unitCode, coord)){
            grid[coord.x][coord.y]++;
            return true;
        }
        return false;

}

    public boolean buildFloor(int unitCode, int x, int y) {
        return buildFloor(unitCode,new Point(x,y));
    }


    public boolean isPlayable(Point coord){
        if (coord.x < 0 || coord.x >= ROW_SIZE || coord.y < 0 || coord.y >= COL_SIZE)
            return false;
        return grid[coord.x][coord.y] != FLOOR_REMOVED;
    }

    public ArrayList<Point> playableArea(int unitCode){
        int x = unitCoord.get(unitCode).x;
        int y = unitCoord.get(unitCode).y;

        ArrayList<Point> area = new ArrayList<>(8);
    //--A unit may move to any neighboring cell, including diagonals.
        // up
        Point up = new Point(x,y-1);
        if(isPlayable(up)) area.add(up);
        // up right
        Point upRight = new Point(x+1,y-1);
        if(isPlayable(upRight)) area.add(upRight);
        // right
        Point right = new Point(x+1,y);
        if(isPlayable(right)) area.add(right);
        // down right
        Point downRight = new Point(x+1,y+1);
        if(isPlayable(downRight)) area.add(downRight);
        // down
        Point down = new Point(x,y+1);
        if(isPlayable(down)) area.add(down);
        // down left
        Point downLeft = new Point(x-1,y+1);
        if(isPlayable(downLeft)) area.add(downLeft);
        // left
        Point left = new Point(x-1,y);
        if(isPlayable(left)) area.add(left);
        // up left
        Point upLeft = new Point(x-1,y-1);



        return area;
    }

    void nextTurn() {
        this.CURRENT_TURN++;
        if (CURRENT_TURN > N_PLAYERS) {
            CURRENT_TURN = 1;
        }


    }

}
