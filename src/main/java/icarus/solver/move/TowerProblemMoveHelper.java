package icarus.solver.move;

import org.optaplanner.core.impl.score.director.ScoreDirector;

import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;

public class TowerProblemMoveHelper
{

   public static void moveCellTower(ScoreDirector scoreDirector,
         CellTower tower, GeodeticLocation2D toLocation)
   {
      scoreDirector.beforeVariableChanged(tower, "towerLocation");
      tower.setLocation(toLocation);
      scoreDirector.afterVariableChanged(tower, "towerLocation");
   }

   private TowerProblemMoveHelper()
   {
   }
}