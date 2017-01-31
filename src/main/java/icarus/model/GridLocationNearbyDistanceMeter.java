package icarus.model;

import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

/**
 * Calculates the distance from a cell phone tower to another geodetic location, for
 * use by OptaPlanner
 */
public class GridLocationNearbyDistanceMeter
      implements NearbyDistanceMeter<CellTower, GeodeticLocation2D>
{
   /**
    * The implementation of the NearbyDistanceMeter interface, providing the
    * distance between two points
    */
   public double getNearbyDistance(CellTower origin, GeodeticLocation2D destination)
   {
      return GeodeticLocation2D.distanceBetween(origin.getLocation(), destination);
   }
}