package icarus.model;

import java.util.ArrayList;
import java.util.Collection;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Represents a cell tower that can service a number of cell phones within
 * it's purview
 * <p>
 * CellTower is a PlanningEntity, meaning that Optaplanner changes it
 * while running scheduling algorithms (namely it changes the tower's
 * location)
 */
@PlanningEntity
public class CellTower
{
   /** The identification number of the tower, should be unique across all towers */
   private int id;
   /**
    * The range of service of this tower, any cell phone within this range can be
    * serviced by this tower, range is defined in kilometers
    */
   private double range;
   /** The location of this tower, this is the variable that changes during scheduling */
   @PlanningVariable(valueRangeProviderRefs = {"locationRange"}) /*
    * , strengthWeightFactoryClass =
    * GridLocationStrengthWeightFactory.class
    */
   private GeodeticLocation2D location;
   /** The types of cell phone frequencies this tower can support */
   private Collection<String> freqTypes;

   //private long score
   //private List<CellPhone> phonesServiced;

   /**
    * No arg constructor so optaplanner can clone towers
    */
   public CellTower()
   {
   }
   
   /**
    * Constructor for a cell phone tower, with no specific frequency types,
    * indicating that it can support all phones
    * @param i The id
    * @param r The range of cell phone service for this tower
    */
   public CellTower(int i, double r)
   {
      this(i, r, null);
   }

   /**
    * Constructor for a cell phone tower
    * @param i The id
    * @param r The range of cell phone service for this tower
    * @param types The types of cell phone frequencies this tower can service
    */
   public CellTower(int i, double r, Collection<String> types)
   {
      id = i;
      range = r;
      location = null;
      freqTypes = types;
   }

   public Collection<String> getFreqTypes()
   {
      return freqTypes;
   }

   public void setFreqType(Collection<String> types)
   {
      freqTypes = types;
   }
   
   public void addFreqType(String type)
   {
      if (freqTypes == null)
      {
         freqTypes = new ArrayList<>();
      }

      freqTypes.add(type);
   }

   public GeodeticLocation2D getLocation()
   {
      return location;
   }

   public void setLocation(GeodeticLocation2D loc)
   {
      location = loc;
   }

   public int getId()
   {
      return id;
   }

   public void setId(int val)
   {
      id = val;
   }

   public double getRange()
   {
      return range;
   }

   public void setRange(double val)
   {
      range = val;
   }

   public void clearLocation()
   {
      location = null;
   }
}
