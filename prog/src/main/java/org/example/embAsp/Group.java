package org.example.embAsp;
import org.example.Game.mode.ai.PlayerAi;
import org.example.Game.mode.ai.actionSet;



public interface Group {
//    /**
//     * Return an instance of this class in {@code embAspSetting} method
//     * when the unit cannot perform an action(Lose).
//     */
//    class NullUnit extends Unit {
//        NullUnit(int unitCode, Player player, Point coord) {
//            super(unitCode, player, coord);
//        }
//    }


    /**
    * Method used to set all the necessary settings for running embAsp<p>
    * @param player
    * @return the unit chosen
    * @throws Exception
    */
    actionSet callEmbAsp(PlayerAi player) throws Exception ;
}
