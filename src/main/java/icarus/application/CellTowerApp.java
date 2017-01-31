package icarus.application;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import icarus.generator.TowerScheduleGenerator;
import icarus.model.CellPhone;
import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;

/**
 * The Cell Tower application uses OptaPlanner to schedule cell phone towers onto a
 * location grid of latitudes and longitudes, attempting to provide coverage for the
 * best combination of high priority cell phones.
 * <p>
 * The solver configuration is defined by an optaplanner defined xml resource -
 * CellTowerSolverConfig.xml
 */
public class CellTowerApp
{
   /** The solver factory that generates an OptaPlanner solver for the problem space */
   private static SolverFactory<TowerSchedule> solverFactory;

   /**
    * Main entrance point for the application, this initializes Optaplanner's solver,
    * creates our cell phone and cell tower lists, and executes the solver, providing
    * metrics and solutions per pre-configured guidelines
    * <p>
    * Configuration is handled via an xml resource - CellTowerSolverConfig.xml
    *
    * @param args The type of scheduling run to perform
    */
   public static void main(String[] args)
   {
      solverFactory = SolverFactory.createFromXmlResource("cellTowerSolverConfig.xml");

      solveForPhonesInArea(10, 2, 4000.00, new GeodeticLocation2D(37.00, -106.00), new GeodeticLocation2D(36.00, -105.00));
   }

   /**
    * Creates a problem space for a solution to be generated for.
    * 
    * @param phones The number of phones in the area
    * @param towers The number of towers to be scheduled
    * @param phoneRange The range of the phones
    * @param topLeft The top left point in the area
    * @param bottomRight The bottom right point in the area
    */
   private static void solveForPhonesInArea(int phones,
         int towers,
         double phoneRange,
         GeodeticLocation2D topLeft,
         GeodeticLocation2D bottomRight)
   {
      Solver<TowerSchedule> solver = solverFactory.buildSolver();
      
      TowerSchedule unsolvedTowerSchedule = new TowerScheduleGenerator()
            .createTowerSchedule(phones, towers, phoneRange, topLeft, bottomRight, 0.1d, 0.1d);

      TowerSchedule solvedTowerSchedule = null;
      solver.solve(unsolvedTowerSchedule);
      solvedTowerSchedule = (TowerSchedule) solver.getBestSolution();
      printTowerScheduleinGeoJSON(solvedTowerSchedule);
   }

   /**
    * Builds a GeoJSON file that can be put in to a viewer to visualize potential
    * solutions
    * 
    * @param solvedTowerSchedule The solution to build the geojson from
    */
   private static void printTowerScheduleinGeoJSON(
         TowerSchedule solvedTowerSchedule)
   {
      System.out.println("Building GEO Json file");
      System.out.println(solvedTowerSchedule.getTowerList().size());

      List<CellTower> initialGuess = solvedTowerSchedule.getTowerList();
      JSONObject featureCollection = new JSONObject();
      try
      {
         featureCollection.put("type", "FeatureCollection");
         JSONArray featureList = new JSONArray();
         List<CellPhone> phoneList = solvedTowerSchedule.getPhoneList();

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
            feature.put("properties", properties);
            featureList.put(feature);
         }
         for (CellTower placedTower : initialGuess)
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
         System.out.println("error");
      }

      System.out.println(featureCollection.toString());

      for (CellTower placed : initialGuess)
      {
         System.out.println("This tower is at ( " + placed.getLocation().getLatitude() + " , "
               + placed.getLocation().getLongitude() + " )");
      }
   }
}
