package icarus.solver.score;

import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import icarus.model.CellPhone;
import icarus.model.CellTower;
import icarus.model.TowerSchedule;
import icarus.util.CellTowerUtil;

public class CellTowerEasyScoreCalc
      implements EasyScoreCalculator<TowerSchedule>
{
   public HardSoftDoubleScore calculateScore(TowerSchedule schedule)
   {
      double hardScore = 0;
      double softScore = 0;

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
            double metricVal = 1.0 / phone.getPriority();
            softScore -= metricVal*metricVal;
         }
      }
      
      return HardSoftDoubleScore.valueOf(hardScore, softScore);
   }
}