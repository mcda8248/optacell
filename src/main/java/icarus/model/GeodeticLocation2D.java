package icarus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a location on a lat/lon grid, altitude is not included
 * 
 * TODO - Fix so lats and lons have to be within appropriate bounds
 *      - Fix distances and clean up circular point calculations
 */
public class GeodeticLocation2D
{
   /** The latitude */
   private double latitude;
   /** The longitude */
   private double longitude;

   /**
    * No argument constructor for jackson
    */
   public GeodeticLocation2D()
   {
   }

   /**
    * Constructor of a two dimensional geodetic location
    * @param lat The latitude
    * @param lon The longitude
    */
   public GeodeticLocation2D(double lat, double lon)
   {
      latitude = lat;
      longitude = lon;
   }
   
   /**
    * Standard getter for latitude
    * @return latitude
    */
   public double getLatitude()
   {
      return latitude;
   }
   /**
    * Standard setter for latitude
    * @param lat What to set latitude to
    */
   public void setLatitude(double lat)
   {
      latitude = lat;
   }
   /**
    * Standard getter for longitude
    * @return longitude
    */
   public double getLongitude()
   {
      return longitude;
   }
   /**
    * Standard setter for longitude
    * @param lon What to set longitude to
    */
   public void setLongitude(double lon)
   {
      longitude = lon;
   }

   /**
    * Returns the distance between two geodetic points in meters
    * @param one location one
    * @param two location two
    * @return The distance between the two points, in meters.  Returns MAX_DOUBLE if either input is null
    */
   public static double distanceBetween(GeodeticLocation2D one,
         GeodeticLocation2D two)
   {
      if (one == null || two == null)
      {
         return Double.MAX_VALUE;
      }

      final double R = 6372800; // Earth Radius in Meters
      double dLat = Math.toRadians(one.getLatitude() - two.getLatitude());
      double dLon = Math.toRadians(one.getLongitude() - two.getLongitude());
      double latRad = Math.toRadians(one.getLatitude());
      double otherLatRad = Math.toRadians(two.getLatitude());

      double a = Math.pow(Math.sin(dLat / 2), 2)
            + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(latRad)
                  * Math.cos(otherLatRad);
      double c = 2 * Math.asin(Math.sqrt(a));
      return R * c;
   }

   /**
    * Returns a collection of points that are every 5 degrees on a circle around a
    * given point at a specified distance.  The first and last points are the same,
    * closing the circle, so the returned array is of size 73
    * @param location The location around which the circle should be calculated
    * @param distance The distance from the center each point should be
    * @return A collection of 73 points, one every 5 degrees in a circle
    */
   public static List<double[]> getPointsInCircleAround(GeodeticLocation2D location,
         double distance)
   {
      List<double[]> points = new ArrayList<>();
      for (int i = 0; i <= 360; i+=5)
      {
         GeodeticLocation2D loc = getLocationAtDistanceFrom(location, distance, i);
         double[] array = new double[2];
         array[1] = loc.getLatitude();
         array[0] = loc.getLongitude();
         points.add(array);
      }
      
      return points;
   }

   /**
    * Returns the point on the earth that is a specified distance from another point
    * in a specified direction
    * @param location The location to start from
    * @param distance The distance to travel, in meters
    * @param angle The angle to travel in from the start
    * @return The end point
    */
   public static GeodeticLocation2D getLocationAtDistanceFrom(GeodeticLocation2D location,
         double distance,
         double angle)
   {
      double lat1 = Math.toRadians(location.getLatitude());
      double lng1 = Math.toRadians(location.getLongitude());
      double brng = Math.toRadians(angle);
      double dRad = distance / (6372797.6);

      double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dRad)
            + Math.cos(lat1) * Math.sin(dRad) * Math.cos(brng));
      double lng2 = lng1
            + Math.atan2(Math.sin(brng) * Math.sin(dRad) * Math.cos(lat1),
                  Math.cos(dRad) - Math.sin(lat1) * Math.sin(lat2));

      return new GeodeticLocation2D(Math.toDegrees(lat2), Math.toDegrees(lng2));
   }

   /**
    * Two geodetic locations are considered equal if their latitudes and longitudes are equal
    * @param other The other object to compare with
    * @return True if the provided object is a GeodeticLocation2D with equal latitude and longitude of this instance
    */
   @Override
   public boolean equals(Object other)
   {
      if (!(other instanceof GeodeticLocation2D))
         return false;
      
      GeodeticLocation2D o = (GeodeticLocation2D)other;
      return (latitude == o.getLatitude() &&
            longitude == o.getLongitude());
   }

   /**
    * Generates the hashcode for a geodetic location, just calls
    * <code>super.hashCode()</code>
    * @see java.lang.Object#hashCode()
    * @return The hashcode of this object
    */
   @Override
   public int hashCode()
   {
      return super.hashCode();
   }
   
   /**
    * Overridden toString method, returns ' (lat, lon) '
    * @see java.lang.Object#toString()
    * @return A string representation of this location
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append(" (")
        .append(latitude)
        .append(", ")
        .append(longitude)
        .append(") ");

      return sb.toString();
   }
}