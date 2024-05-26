package org.example.Game;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    protected final char symbol;
    protected final int playerCode;

    private static int NEXT_UNIT_CODE = 0;
    public record unit(int unitCode, Point coord){} ;
    protected final ArrayList<unit> units;

    public Player(char symbol, int playerCode) {
        this.symbol = symbol;
        this.playerCode = playerCode;
        this.units = new ArrayList<>();
    }

    char getSymbol() {
        return symbol;
    }

    int getPlayerCode() {
        return playerCode;
    }

    unit getUnit(int unitCode) {
        checkUnitCode(unitCode);

        for (unit unit : units) {
            if (unit.unitCode == unitCode) {
                return unit;
            }
        }
        return null;
    }

    unit getUnit(Point coord) {
        checkCoord(coord);
        for (unit unit : units) {
            if (unit.coord.equals(coord)) {
                return unit;
            }
        }

        return null;
    }
    unit getUnit( int x, int y) {
        return getUnit(new Point(x, y));
    }

    /**
     * Get the first unit of the player.<p>
     * Is equivalent to call method {@code getUnits().get(0)}
     * @return
     */
    unit getFirstUnit() {
        return units.getFirst();
    }

    ArrayList<unit> getUnits() {
        return units;
    }
//--UTITILY-------------------------------------------------------------------------------------------------------------
    int addUnit(Point coord){
        checkCoord(coord);

        if (units.size() > Board.UNIT_PER_PLAYER) {
            throw new IllegalStateException("Player already has the maximum number of units");
        }
        units.add(new unit(++NEXT_UNIT_CODE,coord));


        return NEXT_UNIT_CODE;
    }

    /**
     * Move a unit to the given coordinates. <p>
     * Before moving, it checks if the unit is present in the player's units.
     * @param unitCode
     * @param coord
     * @return {@code true} if the unit is present and has been moved, {@code false} otherwise
     */
    boolean moveUnitSafe(int unitCode, Point coord) {
        if (containsUnit(unitCode)){
            int index= units.indexOf(getUnit(unitCode));
            units.get(index).coord.setLocation(coord);
            return true;
        }
        return false;


    }
    /**
     * Check if a unit is present at the given coordinates.
     *
     * @param coord the coordinates to check
     * @return true if a unit is present at the given coordinates, false otherwise
     */
    boolean containsUnit(Point coord) {
        if (units.isEmpty()) return false;

        checkCoord(coord);


        for (unit unit : units) {
            if (unit.coord.equals(coord)) {
                return true;
            }
        }
        return false;
    }
    boolean containsUnit(int x, int y) {
        return containsUnit(new Point(x, y));
    }
    
    boolean containsUnit(int unitCode) {
        checkUnitCode(unitCode);

        for (unit unit : units) {
            if (unit.unitCode == unitCode) {
                return true;
            }
        }
        return false;
    }

//--CHECK EXEPTION------------------------------------------------------------------------------------------------------
    void checkUnitCode(int unitCode) {
        if (unitCode < 1 || unitCode > NEXT_UNIT_CODE) {
            throw new IllegalArgumentException("unitCode must be between 1 and " + NEXT_UNIT_CODE);
        }
    }
    void checkCoord(Point coord) {
        if (coord == null) throw new IllegalArgumentException("coord cannot be null");
        if (coord.x < 0 || coord.x >= Board.ROW_SIZE || coord.y < 0 || coord.y >= Board.COL_SIZE) {
            throw new IllegalArgumentException("coordinate must be inside the grid");
        }
    }


}
