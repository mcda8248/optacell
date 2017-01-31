package icarus.solver.move.factory;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;

public class LocationStrengthWeightFactory
      implements SelectionSorterWeightFactory<TowerSchedule, GeodeticLocation2D>
{
   @Override
   public Comparable<LocationDifficultyWeight> createSorterWeight(TowerSchedule solution,
         GeodeticLocation2D location)
   {
      //TODO - determine where to store how many phones are 'close' to this point
      //int numPhonesHere = location.getPhonesHere().size();

      return new LocationDifficultyWeight(0);
   }

   public static class LocationDifficultyWeight
         implements Comparable<LocationDifficultyWeight>
   {
      private final Integer weight;

      public LocationDifficultyWeight(int w)
      {
         weight = w;
      }

      public int compareTo(LocationDifficultyWeight other)
      {
         return weight.compareTo(other.getWeight());
      }
      
      public Integer getWeight()
      {
         return weight;
      }
   }
}