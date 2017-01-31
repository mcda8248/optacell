package icarus.model;

/**
 * Represents a cell phone that would like cell tower service
 */
public class CellPhone
{
   /** The identification number of the phone, should be unique across all phones */
   private int id;
   /** The range within which a tower must be located to provide service.  Range is defined in kilometers */
   private double range;
   /** The location of the phone */
   private GeodeticLocation2D location;
   /** The type of frequency a tower must support to be able to service this cell phone */
   private String freqType;
   /** The priority of servicing this cell phone for scheduling metrics */
   private int priority;

   /**
    * Constructor for a cell phone
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

   public String getFreqType()
   {
      return freqType;
   }

   public void setFreqType(String type)
   {
      freqType = type;
   }

   public int getPriority()
   {
      return priority;
   }

   public void setPriority(int pri)
   {
      priority = pri;
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
   
   public GeodeticLocation2D getLocation()
   {
      return location;
   }
   
   public void setLocation(GeodeticLocation2D loc)
   {
      location = loc;
   }
}
