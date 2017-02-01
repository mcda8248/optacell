package icarus.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import icarus.model.CellPhone;
import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;
import icarus.util.CellTowerUtil;

/**
 * Creates the problem space for optaplanner to generate a solution for
 */
public class TowerScheduleGenerator
{
   /** logger */
   private static final Logger logger = LoggerFactory.getLogger(TowerScheduleGenerator.class);
   /**
    * Generates a problem space for optaplanner to generate a solution from a properties file
    * @param props The properties to build from
    * @return The generated problem space
    */
   public static TowerSchedule createFromProperties(Properties props)
   {
      TowerSchedule schedule = new TowerSchedule();
      double topLeftLat = Double.valueOf(props.getProperty("grid.topleft.lat"));
      double topLeftLon = Double.valueOf(props.getProperty("grid.topleft.lon"));
      double bottomRightLat = Double.valueOf(props.getProperty("grid.bottomright.lat"));
      double bottomRightLon = Double.valueOf(props.getProperty("grid.bottomright.lon"));
      double latGranularity = Double.valueOf(props.getProperty("grid.lat.granularity"));
      double lonGranularity = Double.valueOf(props.getProperty("grid.lon.granularity"));
      GeodeticLocation2D topLeft = new GeodeticLocation2D(topLeftLat, topLeftLon);
      GeodeticLocation2D bottomRight = new GeodeticLocation2D(bottomRightLat, bottomRightLon);
      schedule.setHighPriorityThreshold(Integer.valueOf(props.getProperty("service.highpri.threshold")));
      schedule.setTopLeft(topLeft);
      schedule.setBottomRight(bottomRight);
      schedule.setLocationList(
            CellTowerUtil.buildGrid(topLeft, bottomRight, latGranularity, lonGranularity));
      
      int numTowers = Integer.valueOf(props.getProperty("grid.towers"));
      int numPhones = Integer.valueOf(props.getProperty("grid.phones"));
      List<String> freqTypes = Stream.of(props.getProperty("frequency.types").split(",")).collect(Collectors.toList());
      GenerationType phonePriType = GenerationType.valueOf(props.getProperty("phone.priority.creation"));
      GenerationType phoneFreqType = GenerationType.valueOf(props.getProperty("phone.freqType.creation"));
      GenerationType phoneRangeType = GenerationType.valueOf(props.getProperty("phone.range.creation"));
      GenerationType towerRangeType = GenerationType.valueOf(props.getProperty("tower.range.creation"));
      GenerationType towerFreqType = GenerationType.valueOf(props.getProperty("tower.freqType.creation"));
      
      //Build phones
      List<CellPhone> phoneList = new ArrayList<>();
      for (int i = 0; i < numPhones; i++)
      {
         int phoneRange = Integer.valueOf(props.getProperty("phone.range.kilometers"));
         if (phoneRangeType == GenerationType.RANDOM)
         {
            int rangeMin = Integer.valueOf(props.getProperty("phone.range.min"));
            int rangeMax = Integer.valueOf(props.getProperty("phone.range.max"));
            phoneRange = CellTowerUtil.generateRandomIntBetween(rangeMin, rangeMax);
         }
         
         GeodeticLocation2D location = CellTowerUtil.generateRandomLocationWithin(topLeft, bottomRight);
         
         String phoneType = props.getProperty("phone.freqType");
         if (phoneFreqType == GenerationType.RANDOM)
         {
            phoneType = freqTypes.get(CellTowerUtil.generateRandomIntBetween(0, freqTypes.size() - 1));
         }
         
         int priority = i+1;
         if (phonePriType == GenerationType.RANDOM)
         {
            int minPri = Integer.valueOf(props.getProperty("phone.priority.min"));
            int maxPri = Integer.valueOf(props.getProperty("phone.priority.max"));
            priority = CellTowerUtil.generateRandomIntBetween(minPri, maxPri);
         }
         phoneList.add(new CellPhone(i+1, phoneRange, location, phoneType, priority));
      }
      schedule.setPhoneList(phoneList);
      
      //Build towers
      List<CellTower> towerList = new ArrayList<>();
      for (int i = 0; i < numTowers; i++)
      {
         int towerRange = Integer.valueOf(props.getProperty("tower.range.kilometers"));
         if (towerRangeType == GenerationType.RANDOM)
         {
            int rangeMin = Integer.valueOf(props.getProperty("tower.range.min"));
            int rangeMax = Integer.valueOf(props.getProperty("tower.range.max"));
            towerRange = CellTowerUtil.generateRandomIntBetween(rangeMin, rangeMax);
         }
         
         List<String> towerFreqTypes = Stream.of(props.getProperty("tower.freqTypes").split(",")).collect(Collectors.toList());
         if (towerFreqType == GenerationType.RANDOM)
         {
            towerFreqTypes.clear();
            while (towerFreqTypes.isEmpty())
            {
               towerFreqTypes = freqTypes.stream()
                     .filter(type -> { return Math.random() < 0.5; })
                     .collect(Collectors.toList());
            }
         }
         towerList.add(new CellTower(i+1, towerRange, towerFreqTypes));
      }
      schedule.setTowerList(towerList);
      
      return schedule;
   }

   /**
    * Creates an initial problem space from a file, which needs to be a saved json TowerSchedule object
    * @param fileToLoad The file to import
    * @return The initial configuration
    */
   public static TowerSchedule createFromImport(String fileToLoad)
   {
      try
      {
         return new ObjectMapper().readValue(new File(fileToLoad), TowerSchedule.class);
      }
         catch (IOException e)
      {
         logger.info("Failed to load initial configuration from file - " + e);
      }

      return null;
   }
}
