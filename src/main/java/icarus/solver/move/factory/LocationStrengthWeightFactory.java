package icarus.solver.move.factory;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;

/**
 * Implementation of Optaplanner's SelectionSorterWeightFactory, which aids some algorithms in
 * finding moves that have a higher likelihood of being 'good' moves.
 * <p>
 * TODO - Weight the locations based on how many phones are within a property defined threshold of them
 */
public class LocationStrengthWeightFactory
      implements SelectionSorterWeightFactory<TowerSchedule, GeodeticLocation2D>
{
   /**
    * Generates the weight of a location within schedule
    * 
    * TODO - Fix so it actually weighs them, based on number of nearby phones
    * 
    * @see org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory#createSorterWeight(org.optaplanner.core.api.domain.solution.Solution, java.lang.Object)
    * @param solution The schedule to use to weight the location
    * @param location The location to generate a weight for
    * @return A LocationDifficultyWeight representing the strength of the provided location
    */
   @Override
   public Comparable<LocationDifficultyWeight> createSorterWeight(TowerSchedule solution,
         GeodeticLocation2D location)
   {
      //TODO - determine where to store how many phones are 'close' to this point
      //int numPhonesHere = location.getPhonesHere().size();

      return new LocationDifficultyWeight(0);
   }

   /**
    * Holds the weight of a location for Optaplanner to use in finding better moves
    */
   public static class LocationDifficultyWeight
         implements Comparable<LocationDifficultyWeight>
   {
      /**
       * The weight, representing how likely a location is to allow a tower to
       *  service many phones there
       */
      private final Integer weight;

      /**
       * Constructor, takes a weight and sets it in this instance
       * @param w The weight to set
       */
      public LocationDifficultyWeight(int w)
      {
         weight = w;
      }

      /**
       * compares a LocationDifficultyWeight to another, simply
       * <code>weight.compareTo(other.getWeight())</code>
       * @return the results of comparing the weights of this object with another
       */
      @Override
      public int compareTo(LocationDifficultyWeight other)
      {
         return weight.compareTo(other.getWeight());
      }

      /**
       * Standard getter for weight
       * @return The weight
       */
      public Integer getWeight()
      {
         return weight;
      }
   }
}