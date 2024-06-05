package org.example.Game.mode.manual;

import org.example.Game.mode.Player;

public class PlayerManual extends Player {
    public PlayerManual(char symbol, int playerCode) {
        super(symbol, playerCode);
    }

    @Override
    public Player copy() {
        return new PlayerManual(this);
    }

    public PlayerManual(PlayerManual player) {
        super(player);
    }
}
