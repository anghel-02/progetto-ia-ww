package org.example.Game.mode.ai;

import org.example.Game.mode.Unit;

import java.awt.*;

/**
 * This class represent a null action.<p>
 * Return an instance of this class in {@code callEmbAsp} when the player cannot move or build.
 */
public class NullAction extends actionSet {
    public NullAction(Unit unit) {
        super(unit, new Point(), new Point());
    }

    @Override
    public Point move(){
        return null;
    }

    @Override
    public Point build(){
        return null;
    }


}
