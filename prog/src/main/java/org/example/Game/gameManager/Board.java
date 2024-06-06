package org.example.Game.gameManager;

import org.example.Game.mode.Player;
import org.example.Game.mode.Unit;
import org.example.embAsp.cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
    private final Player[] players ;
//    private final HashMap<Integer, Point> unitCoord = new HashMap<>(); // unitCode, coord

    //    private final Object lock;
    private boolean win ;

    //--CONSTRUCTOR---------------------------------------------------------------------------------------------------------
    Board(Player player1, Player player2) {
        grid = new int[ROW_SIZE][COL_SIZE];
        players = new Player [N_PLAYERS];
        this.players[0] = player1.copy();
        this.players[1] = player2.copy();

        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < ROW_SIZE; j++) {
                grid[i][j] = FLOOR_START;
            }
        }

        win=false;
    }

public Board copy(){
        Board myBoard = new Board(players[0],players[1]);
        myBoard.setGrid(grid);
        myBoard.win = win;
        return myBoard;
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

    private void setGrid(int[][] grid){
        for (int i = 0; i < ROW_SIZE; i++) {
            this.grid[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public boolean win() {
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
    private Unit unitAt(Point coord){
        return unitAt(coord.x,coord.y);
    }

    public int playerCodeAt(int x, int y){
        for (Player p : players){
            if ( p.isUnit(x,y))
                return p.getPlayerCode();

        }
        return cell.PLAYERCODE_NO_PLAYER;
    }

    public int heightAt(Point coord){
        return heightAt(coord.x,coord.y);
    }

    public int heightAt(int x, int y){
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

    /**
     * Move a unit to a new position. <p>
     * @param unit
     * @param coord
     * @return
     */

//--ACTIONS-------------------------------------------------------------------------------------------------------------
    public boolean moveUnitSafe(Unit unit , Point coord){
        if (canMove(unit,coord)){
            if (!players[unit.player().getPlayerCode()].moveUnitSafe(unit,coord))
                throw new RuntimeException("Qualcosa non va ");

            //--WIN
            if (grid[coord.x][coord.y] == FLOOR_HEIGHT_3)
                setWin();

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

    public boolean buildFloor(Unit unit, Point coord) {
        if (canBuild(unit, coord)){
            grid[coord.x][coord.y]++;
            return true;
        }
        return false;

    }

    public boolean canMove(Point unitCoord, Point toMove){
//    The unit may only move on the same level, step up one level or step down any number of levels.
//    After every movement, the unit must be able to build onto an adjacent cell of its new position.
//    This causes the cell in question to gain 1 unit of height.
//    If the height reaches level 4, the cell is considered removed from play.
//    If your unit moves onto a level 3 cell, you win the game.

        if (! canMethods(unitCoord, toMove)) return false;

        //The unit may only move on the same level, step up one level or step down any number of levels.
        if (grid[toMove.x][toMove.y] - grid[unitCoord.x][unitCoord.y] > 1){
//            System.out.println("NOT VALID! floor too high");
            return false;
        }

        return true;

    }
    public boolean canMove(Unit unit, Point coord){
        return canMove(unit.coord(),coord);
    }

    private boolean canMethods(Point start, Point end){

        int currentX = start.x;
        int currentY = start.y;
        int toX = end.x;
        int toY = end.y;

        //--Can't make action outside the grid
        if (toX < 0 || toX >= ROW_SIZE || toY< 0 || toY >= COL_SIZE)
            return false;

        //--A unit cannot make action to the same cell it is currently in.
        if (start.equals(end)){
//            System.out.println("NOT VALID! same cell");
            return false;
        }

        //--Can make action to any neighboring cell, including diagonals.
        if( Math.abs(toX -currentX)  > 1 || Math.abs(toY-currentY)  > 1){
//            System.out.println("NOT VALID! too far away");
            return false;
        }

        //--A unit cannot make action to a cell that is occupied by another unit.
        if (isOccupied(toX,toY)){
//            System.out.println("NOT VALID! cell occupied");
            return false;
        }

        //--If the height reaches level 4, the cell is considered removed from play.
        if (grid[toX][toY] == FLOOR_REMOVED){
//            System.out.println("NOT VALID! floor removed");
            return false;
        }

        return true;
    }


    private boolean canBuild(Point unitCoord, Point toBuild){
        return canMethods(unitCoord,toBuild);
    }
    public boolean canBuild(Unit unit , Point coord) {
        return canBuild(unit.coord(),coord);
    }




//--EMBASP

    /**
     * Get the area around a cell where a unit can legally move. <p>
     * @param unitCoord the cell where the unit is
     * @return
     */
    public ArrayList<Point> moveableArea(Point unitCoord){
        ArrayList<Point> area = new ArrayList<>(8);
        initArea(area,unitCoord);
        area.removeIf(toMove -> ! canMove(unitCoord,toMove));


        return area;
    }

    /**
     * Get the area around a cell where a unit can legally move. <p>
     * @param unit
     * @return
     */
    public ArrayList<Point> moveableArea(Unit unit){
        return moveableArea(unit.coord());
    }


    /**
     * Get the area around a cell where a unit can legally Build. <p>
     * @param unitCoord the cell where the unit is
     * @return
     */
    public ArrayList<Point> buildableArea(Point unitCoord){
        ArrayList<Point> area = new ArrayList<>(8);
        initArea(area,unitCoord);
        area.removeIf(toBuild -> ! canBuild(unitCoord,toBuild));

        return area;
    }

    /**
     * Get the area around a cell where a unit can legally Build. <p>
     * @param unit
     * @return
     */
    public ArrayList<Point> buildableArea(Unit unit){
        return buildableArea(unit.coord());
    }



    private void initArea( ArrayList<Point> area, Point coord){
        int x = coord.x;
        int y = coord.y;

        //up
        area.add(new Point(x-1,y));
        //up right
        area.add(new Point(x-1,y+1));
        // right
        area.add( new Point(x,y+1));
        // down right
        area.add( new Point(x+1,y+1));
        // down
        area.add(new Point(x+1,y));
        // down left
        area.add( new Point(x+1,y-1));
        // left
        area.add(new Point(x,y-1));
        // up left
        area.add(new Point(x-1,y-1));

    }


}
