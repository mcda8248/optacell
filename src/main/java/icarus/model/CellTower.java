package icarus.model;

import java.util.ArrayList;
import java.util.Collection;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import icarus.scoring.LocationStrengthWeightFactory;

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
    * serviced by this tower, range is defined in meters
    */
   private double range;
   /** The location of this tower, this is the variable that changes during scheduling */
   @PlanningVariable(valueRangeProviderRefs = {"locationRange"},
                     strengthWeightFactoryClass = LocationStrengthWeightFactory.class)
   private GeodeticLocation2D location;
   /** The types of cell phone frequencies this tower can support */
   private Collection<String> freqTypes;

   /**
    * No arg constructor so optaplanner can clone towers, and for jackson
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

   /**
    * Standard getter for freqTypes
    * @return The freqTypes
    */
   public Collection<String> getFreqTypes()
   {
      return freqTypes;
   }

   /**
    * Standard setter for freqTypes
    * @param types The types to set
    */
   public void setFreqType(Collection<String> types)
   {
      freqTypes = types;
   }

   /**
    * Adds a frequency type to the types this tower handles
    * @param type The type to add
    */
   public void addFreqType(String type)
   {
      if (freqTypes == null)
      {
         freqTypes = new ArrayList<>();
      }

      freqTypes.add(type);
   }

   /**
    * Standard getter for location
    * @return The location
    */
   public GeodeticLocation2D getLocation()
   {
      return location;
   }

   /**
    * Standard setter for location
    * @param loc The location to set
    */
   public void setLocation(GeodeticLocation2D loc)
   {
      location = loc;
   }

   /**
    * Standard getter for id
    * @return The id
    */
   public int getId()
   {
      return id;
   }

   /**
    * Standard setter for id
    * @param val The id to set
    */
   public void setId(int val)
   {
      id = val;
   }

   /**
    * Standard getter for range
    * @return The range
    */
   public double getRange()
   {
      return range;
   }

   /**
    * Standard setter for range
    * @param val The range to set
    */
   public void setRange(double val)
   {
      range = val;
   }

   /**
    * Clears the location of this tower, simply sets location to null
    */
   public void clearLocation()
   {
      location = null;
   }
}
