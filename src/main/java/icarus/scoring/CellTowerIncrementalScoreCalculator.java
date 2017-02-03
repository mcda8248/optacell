package icarus.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import icarus.model.CellPhone;
import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;
import icarus.util.CellTowerUtil;

/**
 * Implementation of Optaplanner IncrementalScoreCalculator, should improve performance by not
 * rescoring the entire schedule when moves are performed
 */
public class CellTowerIncrementalScoreCalculator
      implements IncrementalScoreCalculator<TowerSchedule>
{
   /** class logger */
   private static final Logger logger = LoggerFactory.getLogger(CellTowerIncrementalScoreCalculator.class);
   /** The current score of the schedule */
   private HardSoftLongScore score;
   /** All phones whose ranges overlap a location */
   private Map<GeodeticLocation2D, Collection<CellPhone>> phonesServicedByLocation;
   /** Where all the towers currently are */
   private List<GeodeticLocation2D> curTowerLocations;
   /** High priority threshold */
   private int highPriThreshold;
   /** Lowest priority */
   private int lowestPriority;

   /**
    * Resets the score to be the sum of the scoring for all phones in the solution
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#resetWorkingSolution(org.optaplanner.core.api.domain.solution.Solution)
    * @param workingSolution The current working schedule
    */
   @Override
   public void resetWorkingSolution(TowerSchedule workingSolution)
   {
      lowestPriority = workingSolution.getLowestPriority();
      highPriThreshold = workingSolution.getHighPriorityThreshold();
      
      //There should be no towers placed, so this will return the worst possible score
      score = generateScore(workingSolution);
      
      curTowerLocations = new ArrayList<>();
      for (CellTower tower : workingSolution.getTowerList())
      {
         curTowerLocations.add(tower.getLocation());
      }

      phonesServicedByLocation = new HashMap<>();
      workingSolution.getLocationList().stream().forEach(loc -> {
         workingSolution.getPhoneList().stream().forEach(phone -> {
            if (!phonesServicedByLocation.containsKey(loc))
            {
               phonesServicedByLocation.put(loc, new ArrayList<>());
            }

            if (GeodeticLocation2D.distanceBetween(loc, phone.getLocation()) < phone.getRange())
            {
               phonesServicedByLocation.get(loc).add(phone);
            }
         });
      });
   }

   /**
    * Not implemented, cell towers are not added
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#beforeEntityAdded(java.lang.Object)
    * @param entity The entity being added
    */
   @Override
   public void beforeEntityAdded(Object entity)
   {
      //Not implemented
   }

   /**
    * Not implemented, cell towers are not added
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#afterEntityAdded(java.lang.Object)
    * @param entity The entity that was added
    */
   @Override
   public void afterEntityAdded(Object entity)
   {
      place((CellTower)entity);
   }

   /**
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#beforeVariableChanged(java.lang.Object, java.lang.String)
    */
   @Override
   public void beforeVariableChanged(Object entity, String variableName)
   {
      retract((CellTower)entity);
   }

   /**
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#afterVariableChanged(java.lang.Object, java.lang.String)
    */
   @Override
   public void afterVariableChanged(Object entity, String variableName)
   {
      place((CellTower)entity);
   }

   /**
    * Not implemented, Cell Towers are not removed
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#beforeEntityRemoved(java.lang.Object)
    * @param entity The entity being removed
    */
   @Override
   public void beforeEntityRemoved(Object entity)
   {
      retract((CellTower)entity);
   }

   /**
    * Not implemented, Cell Towers are not removed
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#afterEntityRemoved(java.lang.Object)
    * @param entity The entity being removed
    */
   @Override
   public void afterEntityRemoved(Object entity)
   {
      //Not implemented
   }

   /**
    * Updates the score when a tower is removed.  Goes through all phones serviced
    * by the location of the tower and subtracts their score from the total if
    * no other tower remains to service them.
    * @param tower The tower being removed
    */
   private void retract(CellTower tower)
   {
      GeodeticLocation2D curLocation = tower.getLocation();

      //logger.info("Removing tower from " + curLocation);
      if (!curTowerLocations.remove(curLocation))
      {
         logger.info("Failed to remove tower location from list");
      }
      if (curLocation != null && phonesServicedByLocation.containsKey(curLocation))
      {
         for (CellPhone phone : phonesServicedByLocation.get(curLocation))
         {
            //logger.info("phone serviced by location is at " + phone.getLocation());
            boolean stillServiced = curTowerLocations.stream().filter(loc -> {
               return GeodeticLocation2D.distanceBetween(loc, phone.getLocation()) < phone.getRange();
            }).findAny().isPresent();

            if (!stillServiced)
            {
               //logger.info("Phone was found to no longer be serviced for priority - " + phone.getPriority());
               score = score.add(getPhoneScore(phone));
            }
         }
      }
   }

   /**
    * Updates the score when a tower is placed.  Goes through all  phones serviced
    * by the location of the tower and adds their score to the total if no other
    * tower was already servicing them
    * @param tower The tower being placed
    */
   private void place(CellTower tower)
   {
      GeodeticLocation2D newLocation = tower.getLocation();
      
      if (newLocation != null && phonesServicedByLocation.containsKey(newLocation))
      {
         for (CellPhone phone : phonesServicedByLocation.get(newLocation))
         {
            boolean alreadyServiced = curTowerLocations.stream().filter(loc -> {
               return GeodeticLocation2D.distanceBetween(loc, phone.getLocation()) < phone.getRange();
            }).findAny().isPresent();

            if (!alreadyServiced)
            {
               score = score.subtract(getPhoneScore(phone));
            }
         }
      }

      curTowerLocations.add(newLocation);
   }

   /**
    * Returns the current score value
    * @see org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator#calculateScore()
    * @return The current score
    */
   @Override
   public Score<HardSoftLongScore> calculateScore()
   {
      return score;
   }

   /**
    * Returns the score for a cell phone
    * 
    * TODO - consolidate this scoring routine, repeated right now here, in easyscore, and locationweightfactory
    *      - if more than one priority of phone subtracts from hard score, they should probably
    *        subtract more for a pri 1 than a pri 2 also
    * @param phone The phone to score
    * @return The score of the phone
    */
   private HardSoftLongScore getPhoneScore(CellPhone phone)
   {
      long hardScore = 0;
      long softScore = 0;
      
      if (phone.getPriority() <= highPriThreshold)
         hardScore--;
      else
      {
         int relativePri = lowestPriority - phone.getPriority() + 1;
         int cubed = relativePri*relativePri*relativePri;
         softScore -= cubed;
      }
      
      return HardSoftLongScore.valueOf(hardScore, softScore);
   }

   /**
    * Generates the current score of a TowerSchedule.
    * <p>
    * Subtracts a score for each phone in the schedule if it's not being 
    * serviced by a tower
    * <p>
    * TODO - Make the scoring generic or user defined
    *        Discuss and potentially change scoring methodology?
    *        Current scoring takes (lowestPossiblePri - phonePri) ^ 3
    *        and subtracts that from the soft score.  Pri 1 phone subtract 1
    *        from the hard score
    *        
    * @param schedule The tower schedule to score
    * @return The score of the schedule
    */
   public HardSoftLongScore generateScore(TowerSchedule schedule)
   {
      HardSoftLongScore genScore = HardSoftLongScore.valueOf(0, 0);
      
      for (CellPhone phone : schedule.getPhoneList())
      {
         boolean foundTower = false;
         for (CellTower tower : schedule.getTowerList())
         {
            if (CellTowerUtil.isServicing(tower, phone))
            {
               foundTower = true;
               break;
            }
         }
         
         if (!foundTower)
         {
            genScore = genScore.add(getPhoneScore(phone));
         }
      }
      
      return genScore;
   }
}