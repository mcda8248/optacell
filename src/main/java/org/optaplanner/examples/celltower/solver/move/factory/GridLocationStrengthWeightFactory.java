package org.optaplanner.examples.celltower.solver.move.factory;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;
import org.optaplanner.examples.celltower.domain.GridLocation;
import org.optaplanner.examples.celltower.domain.TowerSchedule;

public class GridLocationStrengthWeightFactory
      implements SelectionSorterWeightFactory<TowerSchedule, GridLocation>
{

   @Override
   public Comparable<GridLocationDifficultyWeight> createSorterWeight(TowerSchedule solution,
         GridLocation location)
   {
      int numPhonesHere = location.getPhonesHere().size();

      return new GridLocationDifficultyWeight(numPhonesHere);
   }

   public static class GridLocationDifficultyWeight
         implements Comparable<GridLocationDifficultyWeight>
   {

      //private final GridLocation location;
      private final int numPhonesHere;

      public GridLocationDifficultyWeight(int numPhonesHere)
      {
         //this.location = location;
         this.numPhonesHere = numPhonesHere;
      }

      public int compareTo(GridLocationDifficultyWeight other)
      {
         return new CompareToBuilder()
               .append(other.numPhonesHere, numPhonesHere).toComparison();
      }

   }
}
