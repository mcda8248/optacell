package org.optaplanner.examples.celltower.solver.move;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.ObjectUtils;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.GridLocation;

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
      return !ObjectUtils.equals(tower.getTowerLocation(),
            otherTower.getTowerLocation());
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
      return Arrays.asList(tower.getTowerLocation(),
            otherTower.getTowerLocation());
   }

   @Override
   protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector)
   {
      GridLocation oldTowerLocation = tower.getTowerLocation();
      GridLocation oldOtherTowerLocation = otherTower.getTowerLocation();
      tower.clearLocation();
      tower.clearPhonesServiced();
      otherTower.clearLocation();
      otherTower.clearPhonesServiced();
      TowerProblemMoveHelper.moveCellTower(scoreDirector, tower,
            oldTowerLocation);
      TowerProblemMoveHelper.moveCellTower(scoreDirector, otherTower,
            oldOtherTowerLocation);

   }

}
