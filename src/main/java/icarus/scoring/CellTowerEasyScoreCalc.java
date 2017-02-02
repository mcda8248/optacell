package icarus.scoring;

import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import icarus.model.CellPhone;
import icarus.model.CellTower;
import icarus.model.TowerSchedule;
import icarus.util.CellTowerUtil;

/**
 * Implementation of Optaplanner's EasyScoreCalculator for a TowerSchedule.
 * Generates a double that represents the current score of the schedule, the
 * algorithm used is described in the method implementation documentation
 */
public class CellTowerEasyScoreCalc
      implements EasyScoreCalculator<TowerSchedule>
{
   /**
    * calculates the current score of a TowerSchedule.
    * <p>
    * Subtracts the square of the inverse of the priority of all un-serviced
    * cell phones in the schedule from the soft score.  The hard score is not
    * used at this time
    * <p>
    * Therefore, missing priority 1 cell phones will subtract 1 from the score,
    * and missing priority 2 cell phones will subtract 0.25 from the score, etc...
    * <p>
    * TODO - Make the scoring generic or user defined
    *        Discuss and potentially change scoring methodology?
    *        HardSoftDoubleScore is not recommended, switch to hardsoftlongscore
    *        
    * @param schedule The tower schedule to score
    * @return The score of the schedule
    */
   @Override
   public HardSoftLongScore calculateScore(TowerSchedule schedule)
   {
      long hardScore = 0;
      long softScore = 0;

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
            if (phone.getPriority() <= schedule.getHighPriorityThreshold())
               hardScore--;
            else
            {
               int relativePri = schedule.getLowestPriority() - phone.getPriority();
               int cubed = relativePri*relativePri*relativePri;
               softScore -= cubed;
            }
         }
      }
      
      return HardSoftLongScore.valueOf(hardScore, softScore);
   }
}