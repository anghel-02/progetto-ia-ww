package org.example.Game.mode.manual;

import org.example.Game.mode.Player;

import java.util.Objects;

public class PlayerManual extends Player {
    public PlayerManual(char symbol, int playerCode) {
        super(symbol, playerCode);
    }

    @Override
    public PlayerManual copy() {
        return new PlayerManual(this);
    }

    public PlayerManual(PlayerManual player) {
        super(player);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerManual that = (PlayerManual) o;
        return symbol == that.symbol && playerCode == that.playerCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, playerCode);
    }


}
