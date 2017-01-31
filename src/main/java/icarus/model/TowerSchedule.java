package icarus.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;

/**
 * Represents the schedule of cell phone towers servicing cell phones on a grid
 * <p>
 * This is a PlanningSolution for OptaPlanner purposes
 */
@PlanningSolution
public class TowerSchedule implements Solution<HardSoftDoubleScore>
{
   private Collection<GeodeticLocation2D> locationList;
   private GeodeticLocation2D topLeft;
   private GeodeticLocation2D bottomRight;
   
   @PlanningEntityCollectionProperty
   private List<CellTower> towerList;
   private List<CellPhone> phoneList;
   private HardSoftDoubleScore score;

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
}
