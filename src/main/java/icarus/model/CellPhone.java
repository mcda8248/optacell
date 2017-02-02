package icarus.model;

/**
 * Represents a cell phone that would like cell tower service
 */
public class CellPhone
{
   /** The identification number of the phone, should be unique across all phones */
   private int id;
   /** The range within which a tower must be located to provide service.  Range is defined in meters */
   private double range;
   /** The location of the phone */
   private GeodeticLocation2D location;
   /** The type of frequency a tower must support to be able to service this cell phone */
   private String freqType;
   /** The priority of servicing this cell phone for scheduling metrics */
   private int priority;

   /**
    * No argument constructor for jackson
    */
   public CellPhone()
   {
   }

   /**
    * Constructor for a cell phone
    * @param i The id of the cell phone
    * @param r The range the cell can reach a tower from
    * @param loc The location of the cell phone
    * @param type The type of frequency this cell phone uses
    * @param pri The priority of servicing this cell phone
    */
   public CellPhone(int i, double r, GeodeticLocation2D loc, String type, int pri)
   {
      id = i;
      range = r;
      location = loc;
      freqType = type;
      priority = pri;
   }

   /**
    * Standard getter for freqType
    * @return The phone's frequency type, only towers that handle this type
    *            can service this phone
    */
   public String getFreqType()
   {
      return freqType;
   }

   /**
    * Standard setter for freqType
    * @param type The type to set
    */
   public void setFreqType(String type)
   {
      freqType = type;
   }

   /**
    * Standard getter for priority
    * @return The priority
    */
   public int getPriority()
   {
      return priority;
   }

   /**
    * Standard setter for priority
    * @param pri The priority to set
    */
   public void setPriority(int pri)
   {
      priority = pri;
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
    * Standard setter or id
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
}
