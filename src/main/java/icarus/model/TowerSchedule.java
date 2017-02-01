package icarus.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the schedule of cell phone towers servicing cell phones on a grid
 * <p>
 * This is a PlanningSolution for OptaPlanner purposes
 */
@PlanningSolution
public class TowerSchedule implements Solution<HardSoftDoubleScore>
{
   /** The classes logger */
   private static final Logger logger = LoggerFactory.getLogger(TowerSchedule.class);
   /** The threshold at which a priority should be considered high */
   private int highPriorityThreshold;
   /** A collection of all possible locations that towers can be placed */
   private Collection<GeodeticLocation2D> locationList;
   /** The top left corner of the geospatial area to be considered */
   private GeodeticLocation2D topLeft;
   /** The bottom right corner of the geospatial area to be considered */
   private GeodeticLocation2D bottomRight;
   /**
    * The list of cell towers.  This is an optaplanner PlanningEntityCollectionProperty,
    * as optaplanner changes them while generating it's schedule
    */
   @PlanningEntityCollectionProperty
   private List<CellTower> towerList;
   /** The list of all cell phones to be considered */
   private List<CellPhone> phoneList;
   /** The optaplanner score of this schedule */
   private HardSoftDoubleScore score;

   /**
    * Saves this configuration to a file, simply uses Jackson to save the JSON
    * representation of this instance to the provided file name
    * @param filename The name of the file to store to
    */
   public void saveConfigToFile(String filename)
   {
      ObjectMapper mapper = new ObjectMapper();
      try
      {
         mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), this);
      }
      catch (IOException e)
      {
         logger.info("Failed to write configuration file - " + e);
      }
   }

   /**
    * Returns a geojson representation of the current solution within this schedule
    * @return A geojson string
    */
   public String buildGeoJson()
   {
      logger.info(towerList.size() + " Towers solved.");

      JSONObject featureCollection = new JSONObject();
      try
      {
         featureCollection.put("type", "FeatureCollection");
         JSONArray featureList = new JSONArray();

         //Draw the bounds
         JSONObject boundary = new JSONObject();
         boundary.put("type", "Polygon");
         double [][][] points = new double[1][5][2];
         points[0][0][1] = topLeft.getLatitude();
         points[0][0][0] = topLeft.getLongitude();
         points[0][1][1] = topLeft.getLatitude();
         points[0][1][0] = bottomRight.getLongitude();
         points[0][2][1] = bottomRight.getLatitude();
         points[0][2][0] = bottomRight.getLongitude();
         points[0][3][1] = bottomRight.getLatitude();
         points[0][3][0] = topLeft.getLongitude();
         points[0][4][1] = topLeft.getLatitude();
         points[0][4][0] = topLeft.getLongitude();
         boundary.put("coordinates", points);
         JSONObject boundaryFeature = new JSONObject();
         boundaryFeature.put("geometry", boundary);
         boundaryFeature.put("type", "Feature");
         JSONObject featureProps = new JSONObject();
         featureProps.put("id", 999);
         featureProps.put("fill", "yellow");
         boundaryFeature.put("properties", featureProps);
         featureList.put(boundaryFeature);
         
         for (CellPhone phone : phoneList)
         {

            JSONObject polygon = new JSONObject();
            polygon.put("type", "Polygon");
            double[][][] polyPoints = new double[1][9][2];
            int phoneCircleCount = 0;
            for (int i = 0; i < 8; i++)
            {
               polyPoints[0][i] =
                     GeodeticLocation2D.getPointsInCircleAround(phone.getLocation(),
                           phone.getRange()).get(phoneCircleCount);
               phoneCircleCount++;
            }
            polyPoints[0][8] = GeodeticLocation2D.getPointsInCircleAround(phone.getLocation(),
                  phone.getRange()).get(0);
            polygon.put("coordinates", polyPoints);
            JSONObject feature = new JSONObject();
            feature.put("geometry", polygon);
            JSONObject properties = new JSONObject();
            properties.put("id", phone.getId());
            feature.put("type", "Feature");
            if (phone.getPriority() <= highPriorityThreshold)
               properties.put("fill", "red");
            feature.put("properties", properties);
            featureList.put(feature);
         }
         for (CellTower placedTower : towerList)
         {
            JSONObject polygon = new JSONObject();
            polygon.put("type", "Polygon");
            double[][][] polyPoints = new double[1][9][2];
            int towerCircleCount = 0;
            for (int i = 0; i < 8; i++)
            {
               polyPoints[0][i] = GeodeticLocation2D.getPointsInCircleAround(placedTower.getLocation(), placedTower.getRange())
                     .get(towerCircleCount);
               towerCircleCount++;
            }
            polyPoints[0][8] = GeodeticLocation2D.getPointsInCircleAround(placedTower.getLocation(), placedTower.getRange()).get(0);
            polygon.put("coordinates", polyPoints);

            JSONObject feature = new JSONObject();
            feature.put("geometry", polygon);
            JSONObject properties = new JSONObject();
            properties.put("fill", "#32CD32");
            feature.put("type", "Feature");
            feature.put("properties", properties);
            featureList.put(feature);
         }
         featureCollection.put("features", featureList);
      } catch (JSONException e)
      {
         logger.error("Error building JSON - " + e);
      }
      
      for (CellTower placed : towerList)
      {
         logger.info("This tower is at ( " + placed.getLocation().getLatitude() + " , "
               + placed.getLocation().getLongitude() + " )");
      }

      return featureCollection.toString();
   }

   /**
    * Returns the list of the possible locations within the geospatial area of 
    * effect of this schedule.
    * <p>
    * This is an Optaplanner ValueRangeProvider, which means it's used by optaplanner
    * to determine all the possible values for it's planning entities (cell towers)
    * TODO - Would be nice if this could be an Optaplanner ValueRange instead of a static list?
    * @return The list of all locations
    */
   @ValueRangeProvider(id = "locationRange")
   public Collection<GeodeticLocation2D> getLocationList()
   {
      return locationList;
   }

   /**
    * Standard setter for the locations
    * @param val The locations to set
    */
   public void setLocationList(Collection<GeodeticLocation2D> val)
   {
      locationList = val;
   }

   /**
    * Standard getter for the tower list
    * @return The tower list
    */
   public List<CellTower> getTowerList()
   {
      return towerList;
   }

   /**
    * Standard setter for the tower list
    * @param list The tower list to set
    */
   public void setTowerList(List<CellTower> list)
   {
      towerList = list;
   }

   /**
    * Standard getter for the phone list
    * @return The phone list
    */
   public List<CellPhone> getPhoneList()
   {
      return phoneList;
   }

   /**
    * Standard setter for the phone list
    * @param val The phone list to set
    */
   public void setPhoneList(List<CellPhone> val)
   {
      phoneList = val;
   }

   /**
    * Standard getter for the top left corner of the geospatial area
    * @return The top left corner
    */
   public GeodeticLocation2D getTopLeft()
   {
      return topLeft;
   }

   /**
    * Standard setter for the top left corner of the geospatial area
    * @param val The top left corner to set
    */
   public void setTopLeft(GeodeticLocation2D val)
   {
      topLeft = val;
   }

   /**
    * Standard getter for the bottom right corner of the geospatial area
    * @return The bottom right corner
    */
   public GeodeticLocation2D getBottomRight()
   {
      return bottomRight;
   }

   /**
    * Standard setter for the bottom right corner of the geospatial area
    * @param val The bottom right corner to set
    */
   public void setBottomRight(GeodeticLocation2D val)
   {
      bottomRight = val;
   }

   /**
    * Returns all Optaplanner problem facts.  In this case, these are all the
    * locations that towers can be moved to
    * TODO - Verify this, what if the locations were ranges instead of a finite list?
    * @see org.optaplanner.core.api.domain.solution.Solution#getProblemFacts()
    * @return A collection of all problem facts
    */
   @Override
   public Collection<? extends Object> getProblemFacts()
   {
      List<Object> facts = new ArrayList<>();
      facts.addAll(locationList);

      return facts;
   }

   /**
    * Standard getter for the current score of this solution
    * @see org.optaplanner.core.api.domain.solution.Solution#getScore()
    * @return The score
    */
   @Override
   public HardSoftDoubleScore getScore()
   {
      return this.score;
   }

   /**
    * Standard setter for the current score of this solution
    * @see org.optaplanner.core.api.domain.solution.Solution#setScore(org.optaplanner.core.api.score.Score)
    * @param val The score to set
    */
   @Override
   public void setScore(HardSoftDoubleScore val)
   {
      score = val;
   }

   /**
    * Standard getter for the high priority threshold
    * @return The high priority threshold
    */
   public int getHighPriorityThreshold()
   {
      return highPriorityThreshold;
   }

   /**
    * Standard setter for the high priority threshold
    * @param val The threshold to set
    */
   public void setHighPriorityThreshold(int val)
   {
      highPriorityThreshold = val;
   }
}
