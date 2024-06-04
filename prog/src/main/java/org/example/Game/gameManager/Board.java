package org.example.Game.gameManager;

import org.example.Game.mode.Player;
import org.example.Game.mode.Unit;
import org.example.embAsp.cell;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Class that represents the game board.
 */
public class Board {
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
//    private final HashMap<Integer, Point> unitCoord = new HashMap<>(); // unitCode, coord

    //    private final Object lock;
    private boolean win = false;

    //--CONSTRUCTOR---------------------------------------------------------------------------------------------------------
    Board(Player[] players) {
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

    public Player[] getPlayers() {
        return players;
    }

    public boolean Win() {
        return win;
    }
    public void setWin() {
        win = true;
    }

//--UTILITY--------------------------------------------------------------------------------------------------------------
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
        for (Player p : players){
            if ( p.isUnit(x,y))
                return true;
        }

        return false;
    }

    private Unit unitAt(int x, int y){
        for (Player p : players){
            if ( p.unitAt(x,y) != null)
                return p.unitAt(x,y);

        }
        return null;
    }

    int playerCodeAt(int x, int y){
        for (Player p : players){
            if ( p.isUnit(x,y))
                return p.getPlayerCode();

        }
        return cell.PLAYERCODE_NO_PLAYER;
    }

    public int heightAt(Point coord){
        return heightAt(coord.x,coord.y);
    }

    int heightAt(int x, int y){
        return grid[x][y];
    }


//--GAME----------------------------------------------------------------------------------------------------------------
    public void display() {
        String [][] display = new String[ROW_SIZE][COL_SIZE];
        System.out.println();

    //--REFRESH toDisplay
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                display[i][j] = "  " + grid[i][j] +"  ";
            }
        }

        for (Player p : players){
            for (Unit u : p.getUnits()){
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
        return players[playerCode].addUnit(coord);

    }

    public int addUnit(char symbol, int x, int y) {
        return addUnit(symbol, new Point(x,y));

    }

    public boolean canMove(Unit unit, Point coordMove){
//    A unit may move to any neighboring cell, including diagonals.
//    The unit may only move on the same level, step up one level or step down any number of levels.

//    After every movement, the unit must be able to build onto an adjacent cell of its new position.
//    This causes the cell in question to gain 1 unit of height.
//    If the height reaches level 4, the cell is considered removed from play.
//    If your unit moves onto a level 3 cell, you win the game.


        int currentX = unit.coord().x;
        int currentY = unit.coord().y;
        int toMoveX = coordMove.x;
        int toMoveY = coordMove.y;

        //--A unit cannot move to the same cell it is currently in.
        if (unit.coord().equals(coordMove)){
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

    public boolean canMove(Unit unit, int x, int y){
        return canMove(unit, new Point(x,y));
    }



    /**
     * Move a unit to a new position. <p>
     * @param unit
     * @param coord
     * @return
     */
    public boolean moveUnitSafe(Unit unit , Point coord){
        if (canMove(unit,coord)){
            players[unit.player().getPlayerCode()].moveUnitSafe(unit,coord);

            //--WIN
            if (grid[coord.x][coord.y] == FLOOR_HEIGHT_3){
                win = true;
            }

            return true;
        }
        return false;
    }

    /**
     * Move a unit to a new position. <p>
     * @param unit
     * @param x
     * @param y
     * @return
     */
    public boolean moveUnitSafe(Unit unit, int x, int y){
        return moveUnitSafe(unit, new Point(x,y));
    }


    public boolean canBuild(Unit unit , Point coordBuild) {
        int currentX = unit.coord().x;
        int currentY = unit.coord().y;
        int toBuildX = coordBuild.x;
        int toBuildY = coordBuild.y;

        //--A unit cannot build to the same cell it is currently in.
        if (unit.coord().equals(coordBuild)){
            System.out.println("NOT VALID! same cell");
            return false;
        }

        //--A unit cannot build to a cell that is occupied by another unit.
        if (isOccupied(coordBuild)){
            System.out.println("NOT VALID! cell occupied");
            return false;
        }

        //--A unit may build to any neighboring cell, including diagonals.
        if( Math.abs(toBuildX-currentX)  > 1 || Math.abs(toBuildY-currentY)  > 1){
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

    public boolean buildFloor(Unit unit, Point coord) {
        if (canBuild(unit, coord)){
            grid[coord.x][coord.y]++;
            return true;
        }
        return false;

    }



    public boolean isPlayable(Point coord){
        if (coord.x < 0 || coord.x >= ROW_SIZE || coord.y < 0 || coord.y >= COL_SIZE)
            return false;
        return grid[coord.x][coord.y] != FLOOR_REMOVED;
    }

    /**
     * Get the playable area for a unit. <p>
     *  A unit may move to any neighboring cell, including diagonals.
     * @param unit
     * @return
     */
    public ArrayList<Point> playableArea(Unit unit){
        int x = unit.coord().x;
        int y = unit.coord().y;
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


}
