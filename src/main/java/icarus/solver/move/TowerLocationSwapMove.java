package icarus.solver.move;

import java.util.Arrays;
import java.util.Collection;

import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;

public class TowerLocationSwapMove extends AbstractMove
{

   private CellTower tower;
   private CellTower otherTower;

   public TowerLocationSwapMove(CellTower tower, CellTower otherTower)
   {
      this.tower = tower;
      this.otherTower = otherTower;
   }

   @Override
   public boolean isMoveDoable(ScoreDirector scoreDirector)
   {
      return tower.getLocation() != null && !tower.getLocation().equals(otherTower.getLocation());
   }

   @Override
   public Move createUndoMove(ScoreDirector scoreDirector)
   {
      return new TowerLocationSwapMove(tower, otherTower);
   }

   @Override
   public Collection<? extends Object> getPlanningEntities()
   {
      return Arrays.asList(tower, otherTower);
   }

   @Override
   public Collection<? extends Object> getPlanningValues()
   {
      return Arrays.asList(tower.getLocation(),
            otherTower.getLocation());
   }

   @Override
   protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector)
   {
      GeodeticLocation2D oldTowerLocation = tower.getLocation();
      GeodeticLocation2D oldOtherTowerLocation = otherTower.getLocation();
      tower.clearLocation();
      otherTower.clearLocation();
      TowerProblemMoveHelper.moveCellTower(scoreDirector, tower,
            oldTowerLocation);
      TowerProblemMoveHelper.moveCellTower(scoreDirector, otherTower,
            oldOtherTowerLocation);
   }
}