package org.example.Game.mode;

import org.example.Game.gameManager.Board;

import java.awt.*;
import java.util.ArrayList;

public abstract class Player {
    protected final char symbol;
    protected final int playerCode;

    private static int NEXT_UNIT_CODE = 0;

//    public record unit(int unitCode, int playerCode, Point coord){ };
    protected final ArrayList<Unit> Units;

    protected Player(char symbol, int playerCode) {
        this.symbol = symbol;
        this.playerCode = playerCode;
        this.Units = new ArrayList<>();
    }

    public char getSymbol() {
        return symbol;
    }

    public int getPlayerCode() {
        return playerCode;
    }


    /**
     * Get the first unit of the player.<p>
     * Is equivalent to call method {@code getUnits().get(0)}
     * @return
     */
    public Unit getFirstUnit() {
        return Units.getFirst();
    }

    public ArrayList<Unit> getUnits() {
        return Units;
    }

//--UTITILY-------------------------------------------------------------------------------------------------------------
    public int addUnit(Point coord){
        checkCoord(coord);

        if (Units.size() > Board.UNIT_PER_PLAYER) {
            throw new IllegalStateException("Player already has the maximum number of units");
        }
        Units.add(new Unit(++NEXT_UNIT_CODE, this.playerCode, coord));


        return NEXT_UNIT_CODE;
    }

    /**
     * Check if a unit is present at the given coordinates.
     *
     * @param coord the coordinates to check
     * @return true if a unit is present at the given coordinates, false otherwise
     */
    public boolean isUnit(Point coord) {
        checkCoord(coord);

        for (Unit unit : Units) {
            if (unit.coord.equals(coord)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUnit(int x, int y) {
        return isUnit(new Point(x, y));
    }

    public boolean containsUnit(Unit unit) {
        checkUnitCode(unit.unitCode);

        for (Unit u : Units) {
            if (u.equals(unit)) {
                return true;
            }
        }
        return false;
    }

    public Unit unitAt(Point coord) {
        checkCoord(coord);
        for (Unit unit : Units) {
            if (unit.coord.equals(coord)) {
                return unit;
            }
        }
        return null;
    }

    public Unit unitAt(int x, int y) {
        return unitAt(new Point(x, y));
    }

    /**
     * Move a unit to the given coordinates. <p>
     * Before moving, it checks if the unit is present in the player's units.
     * @param unit
     * @param coord
     * @return {@code true} if the unit is present and has been moved, {@code false} otherwise
     */
    public boolean moveUnitSafe(Unit unit, Point coord) {
        if (containsUnit(unit)){
            for (Unit u : Units) {
                if (u.equals(unit)) {
                    u.coord.setLocation(coord);
                    return true;
                }
            }
            return true;
        }
        return false;


    }

    //--CHECK EXEPTION------------------------------------------------------------------------------------------------------
    protected void checkUnitCode(int unitCode) {
        if (unitCode < 1 || unitCode > NEXT_UNIT_CODE) {
            throw new IllegalArgumentException("unitCode must be between 1 and " + NEXT_UNIT_CODE);
        }
    }
    protected void checkCoord(Point coord) {
        if (coord == null) throw new IllegalArgumentException("coord cannot be null");
        if (coord.x < 0 || coord.x >= Board.ROW_SIZE || coord.y < 0 || coord.y >= Board.COL_SIZE) {
            throw new IllegalArgumentException("coordinate must be inside the grid");
        }
    }

}
