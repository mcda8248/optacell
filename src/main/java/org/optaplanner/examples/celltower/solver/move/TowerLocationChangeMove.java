package org.optaplanner.examples.celltower.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.GridLocation;

public class TowerLocationChangeMove extends AbstractMove
{

   private CellTower tower;
   private GridLocation toLocation;

   public TowerLocationChangeMove(CellTower tower, GridLocation toLocation)
   {
      this.tower = tower;
      this.toLocation = toLocation;
   }

   public boolean isMoveDoable(ScoreDirector scoreDirector)
   {
      return !ObjectUtils.equals(tower.getTowerLocation(), toLocation);
   }

   public Move createUndoMove(ScoreDirector scoreDirector)
   {
      return new TowerLocationChangeMove(tower, tower.getTowerLocation());
   }

   public Collection<? extends Object> getPlanningEntities()
   {
      return Collections.singletonList(tower);
   }

   public Collection<? extends Object> getPlanningValues()
   {
      return Collections.singletonList(toLocation);
   }

   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      } else if (o instanceof TowerLocationChangeMove)
      {
         TowerLocationChangeMove other = (TowerLocationChangeMove) o;
         return new EqualsBuilder().append(tower, other.tower)
               .append(toLocation, other.toLocation).isEquals();
      } else
      {
         return false;
      }
   }

   public int hashCode()
   {
      return new HashCodeBuilder().append(tower).append(toLocation)
            .toHashCode();
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
      tower.clearPhonesServiced();
      TowerProblemMoveHelper.moveCellTower(scoreDirector, tower, toLocation);

   }

}
