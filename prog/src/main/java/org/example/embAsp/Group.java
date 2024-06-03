package org.example.embAsp;


import org.example.Game.mode.ai.PlayerAi;
import org.example.Game.mode.Unit;

public interface Group {
     /**
      * Method used to set all the necessary settings for running embAsp
      * @param player
      * @return the unit chosen
      * @throws Exception
      */
     Unit embAspSetting(PlayerAi player) throws Exception ;
}
