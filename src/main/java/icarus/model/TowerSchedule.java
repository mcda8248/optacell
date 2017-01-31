package icarus.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
   private static final Logger logger = LoggerFactory.getLogger(TowerSchedule.class);
   private int highPriorityThreshold;
   private Collection<GeodeticLocation2D> locationList;
   private GeodeticLocation2D topLeft;
   private GeodeticLocation2D bottomRight;
   
   @PlanningEntityCollectionProperty
   private List<CellTower> towerList;
   private List<CellPhone> phoneList;
   private HardSoftDoubleScore score;

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

   @ValueRangeProvider(id = "locationRange")
   public Collection<GeodeticLocation2D> getLocationList()
   {
      return locationList;
   }

   public void setLocationList(Collection<GeodeticLocation2D> locationList)
   {
      this.locationList = locationList;
   }

   public List<CellTower> getTowerList()
   {
      return towerList;
   }

   public void setTowerList(List<CellTower> towerList)
   {
      this.towerList = towerList;
   }

   public List<CellPhone> getPhoneList()
   {
      return phoneList;
   }

   public void setPhoneList(List<CellPhone> phoneList)
   {
      this.phoneList = phoneList;
   }

   public GeodeticLocation2D getTopLeft()
   {
      return topLeft;
   }

   public void setTopLeft(GeodeticLocation2D topLeft)
   {
      this.topLeft = topLeft;
   }

   public GeodeticLocation2D getBottomRight()
   {
      return bottomRight;
   }

   public void setBottomRight(GeodeticLocation2D bottomRight)
   {
      this.bottomRight = bottomRight;
   }

   @Override
   public Collection<? extends Object> getProblemFacts()
   {
      List<Object> facts = new ArrayList<>();
      facts.addAll(locationList);

      return facts;
   }

   @Override
   public HardSoftDoubleScore getScore()
   {
      return this.score;
   }

   @Override
   public void setScore(HardSoftDoubleScore score)
   {
      this.score = score;
   }

   public int getHighPriorityThreshold()
   {
      return highPriorityThreshold;
   }

   public void setHighPriorityThreshold(int highPriorityThreshold)
   {
      this.highPriorityThreshold = highPriorityThreshold;
   }
}
