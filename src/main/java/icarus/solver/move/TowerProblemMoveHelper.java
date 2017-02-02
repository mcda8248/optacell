package icarus.solver.move;

import org.optaplanner.core.impl.score.director.ScoreDirector;

import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;

/**
 * TODO Enter comment, understand what this method is doing first
 */
public class TowerProblemMoveHelper
{
   /**
    * TODO Add method comment after understanding the method
    * @param scoreDirector Optaplanner object that holds it's current working solution
    * @param tower A tower being moved
    * @param toLocation The location the tower is moved to
    */
   public static void moveCellTower(ScoreDirector scoreDirector,
         CellTower tower, GeodeticLocation2D toLocation)
   {
      scoreDirector.beforeVariableChanged(tower, "towerLocation");
      tower.setLocation(toLocation);
      scoreDirector.afterVariableChanged(tower, "towerLocation");
   }
}