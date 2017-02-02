package icarus.scoring;

import java.util.List;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import icarus.model.CellPhone;
import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;

/**
 * Implementation of an Optaplanner SelectionSorterWeightFacotry.  Returns a comparable that
 * weighs Geodetic locations by the amount of scoring it can decrease by placing a tower there
 */
public class LocationStrengthWeightFactory
      implements SelectionSorterWeightFactory<TowerSchedule, GeodeticLocation2D>
{
   public static long callCount = 0;
   /**
    * Generates a comparable that allows Optaplanner to determine the relative weights
    * of geodetic locations
    * @see org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory#createSorterWeight(org.optaplanner.core.api.domain.solution.Solution, java.lang.Object)
    * @param solution The current schedule, containing the phone list
    * @param location The location
    * @return A comparable that will compare the provided location with others based on the phones
    */
   @Override
   public Comparable<LocationWeight> createSorterWeight(TowerSchedule solution,
         GeodeticLocation2D location)
   {
      return new LocationWeight(location, solution.getPhoneList());
   }
   
   /**
    * The class this factory will return
    */
   public static class LocationWeight implements Comparable<LocationWeight>
   {
      /** The location */
      private GeodeticLocation2D location;
      /** The score of this location */
      private Double score;

      
      /**
       * Constructor, takes the location and the list of phones to check against
       * @param loc The location to set
       * @param phoneList The list of phones to set
       */
      public LocationWeight(GeodeticLocation2D loc, List<CellPhone> phoneList)
      {
         location = loc;
         score = phoneList.stream()
               .mapToDouble(p ->
               {
                  if (GeodeticLocation2D.distanceBetween(loc, p.getLocation()) < p.getRange())
                  {
                     return Math.pow(1.0 / p.getPriority(), 2);
                  }

                  return 0.0;
               }).sum();
      }

      /**
       * Standard getter for location
       * @return the location
       */
      public GeodeticLocation2D getLocation()
      {
         return location;
      }

      /**
       * Standard getter for score
       * @return the score
       */
      public Double getScore()
      {
         return score;
      }

      /**
       * Compares two geodetic locations by the amount of scoring that will be
       * decreased if a tower is placed there
       * @see java.lang.Comparable#compareTo(java.lang.Object)
       * @param o The location to compare to
       * @return The results of the comparison
       */
      @Override
      public int compareTo(LocationWeight o)
      {
         callCount++;
         int val = score.compareTo(o.getScore());
         
         return val == 0 ? 1 : val;
      }
   }
}