package icarus.util;

import java.util.ArrayList;
import java.util.Collection;

import icarus.model.CellPhone;
import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;

/**
 * Utility methods to aid in cell tower scheduling
 */
public class CellTowerUtil
{
   /**
    * Determines if a tower is servicing a cell phone.  Checks that frequency types match
    * first to avoid distance calculations if possible, then determines if the tower is
    * closer than the smaller of the service range between the phone and tower.
    * @param tower The cell phone tower
    * @param phone The cell phone
    * @return true if the tower is servicing the phone, false otherwise
    */
   public static boolean isServicing(CellTower tower, CellPhone phone)
   {
      if (tower.getFreqTypes() != null &&
            phone.getFreqType() != null &&
            !tower.getFreqTypes().contains(phone.getFreqType()))
      {
         return false;
      }
      
      double smallerRange = phone.getRange();
      if (tower.getRange() < phone.getRange())
      {
         smallerRange = tower.getRange();
      }

      return GeodeticLocation2D.distanceBetween(tower.getLocation(), phone.getLocation()) < smallerRange;
   }

   /**
    * Creates a grid of geodetic locations that towers can be placed on
    *
    * @param topLeft The top left of the grid
    * @param bottomRight The bottom right of the grid
    * @param latGranularity The distance between grid points in the latitudinal direction
    * @param lonGranularity The distance between grid points in the longitudinal direction
    * @return A collection of geodetic points defining all possible points on the grid
    */
   public static Collection<GeodeticLocation2D> buildGrid(GeodeticLocation2D topLeft,
         GeodeticLocation2D bottomRight,
         double latGranularity,
         double lonGranularity)
   {
      Collection<GeodeticLocation2D> grid = new ArrayList<>();
      double startLat = topLeft.getLatitude();
      double startLon = topLeft.getLongitude();
      double endLat = bottomRight.getLatitude();
      double endLon = bottomRight.getLongitude();
      double curLat = startLat;
      double curLon = startLon;
      
      //Top left latitude should always be bigger than bottom right lat
      //Top left longitude should always be  less than bottom right lon
      while (curLat >= endLat)
      {
         while (curLon <= endLon)
         {
            grid.add(new GeodeticLocation2D(curLat, curLon));
            curLon += lonGranularity;
         }
         curLat -= latGranularity;
         curLon = startLon;
      }

      return grid;
   }
}