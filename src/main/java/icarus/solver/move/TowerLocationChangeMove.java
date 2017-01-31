package icarus.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;

public class TowerLocationChangeMove extends AbstractMove
{
   private CellTower tower;
   private GeodeticLocation2D toLocation;

   public TowerLocationChangeMove(CellTower tower, GeodeticLocation2D toLocation)
   {
      this.tower = tower;
      this.toLocation = toLocation;
   }

   public boolean isMoveDoable(ScoreDirector scoreDirector)
   {
      return tower.getLocation() != null && !tower.getLocation().equals(toLocation);
   }

   public Move createUndoMove(ScoreDirector scoreDirector)
   {
      return new TowerLocationChangeMove(tower, tower.getLocation());
   }

   public Collection<? extends Object> getPlanningEntities()
   {
      return Collections.singletonList(tower);
   }

   public Collection<? extends Object> getPlanningValues()
   {
      return Collections.singletonList(toLocation);
   }

   public String toString()
   {
      return tower + " => " + toLocation;
   }

   @Override
   public String getSimpleMoveTypeDescription()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector)
   {
      tower.clearLocation();
      TowerProblemMoveHelper.moveCellTower(scoreDirector, tower, toLocation);
   }
}