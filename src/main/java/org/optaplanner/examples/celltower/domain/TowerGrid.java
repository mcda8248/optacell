package org.optaplanner.examples.celltower.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TowerGrid
{

   private double gridSize;
   private ArrayList<GridLocation> grid = new ArrayList<>();
   private int gridOffSet = 0;
   private List<List<GridLocation>> latLines = new ArrayList<>();
   private long optimalScore;
   private List<CellPhone> phonesInGrid = new ArrayList<>();
   private long actualScore;

   public TowerGrid(double range, double topLeftCornerLat,
         double topLeftCornerLng, double botLeftCornerLat,
         double topRightCornerLng, List<CellPhone> phones,
         List<CellTower> towers)
   {
     //double areaOfSearch = metersBetweenTwoPoints(topLeftCornerLat,
      //      topLeftCornerLng, topLeftCornerLat, topRightCornerLng);
      gridSize = Math.sqrt((range * range) / 2);
      double currentLng = topLeftCornerLng;
      double currentLat = topLeftCornerLat;
      this.phonesInGrid = phones;
      int gridId = 0;
      List<GridLocation> currentLine = new ArrayList<>();
      while (currentLat > botLeftCornerLat)
      {
         List<CellPhone> phonesHere = new ArrayList<>();
         List<Integer> priHere = new ArrayList<>();
         if (currentLng > topRightCornerLng)
         {
            this.latLines.add(currentLine);

            currentLine = new ArrayList<>();

            currentLng = topLeftCornerLng;
            currentLat = distFromPointWithDistance(currentLat, currentLng,
                  gridSize, 180.0)[0];
         }
         long gridLocationScore = 0;
         for (CellPhone phone : phones)
         {
            if (metersBetweenTwoPoints(currentLat, currentLng, phone.getLat(),
                  phone.getLng()) <= 9000.00 + phone.getRange())
            {
               gridLocationScore = gridLocationScore + phone.getValue();
               phonesHere.add(phone);
               priHere.add(phone.getPri());
            }
         }
         GridLocation thisLocation = new GridLocation(currentLat, currentLng,
               gridLocationScore, gridId);
         thisLocation.setPhonesHere(phonesHere);
         thisLocation.setPriHere(priHere);

         grid.add(thisLocation);
         currentLine.add(thisLocation);
         currentLng = distFromPointWithDistance(currentLat, currentLng,
               gridSize, 90.0)[1];
         gridId++;
      }
      // for (GridLocation loc : grid) {
      // System.out.println("Grid "+loc.getId()+" score is "+loc.getScore());
      // }
      List<GridLocation> testGrid = new ArrayList<>();
      for (GridLocation loc : this.grid)
      {
         GridLocation fresh = new GridLocation(loc.getLat(), loc.getLng(),
               loc.getScore(), loc.getId());

         fresh.setPhonesHere(loc.getPhonesHere());
         testGrid.add(fresh);

      }
      makeInitialGuess(towers.size(), testGrid);

   }

   public List<List<GridLocation>> getLatLines()
   {
      return latLines;
   }

   public void setLatLines(List<List<GridLocation>> latLines)
   {
      this.latLines = latLines;
   }

   public ArrayList<GridLocation> recalcScores(ArrayList<GridLocation> grid,
         List<CellPhone> phones)
   {
      List<CellPhone> phonesToRemove = new ArrayList<>();
      for (CellPhone phone : phones)
      {
         phonesToRemove.add(phone);
      }
      for (GridLocation gridLocation : grid)
      {
         List<CellPhone> toRemove = new ArrayList<>();
         for (Iterator<CellPhone> itr = phonesToRemove.iterator(); itr
               .hasNext();)
         {
            CellPhone phone = itr.next();
            for (CellPhone here : gridLocation.getPhonesHere())
            {

               if (here.getId() == phone.getId())
               {
                  toRemove.add(phone);
                  gridLocation
                        .setScore(gridLocation.getScore() - phone.getValue());
               }

            }
         }

         gridLocation.getPhonesHere().removeAll(toRemove);

      }
      return grid;
   }

   public double metersBetweenTwoPoints(double thisLat, double thisLng,
         double thatLat, double thatLng)
   {
      final double R = 6372800; // Earth Radius in Meters
      double dLat = Math.toRadians(thisLat - thatLat);
      double dLon = Math.toRadians(thisLng - thatLng);
      double latRad = Math.toRadians(thisLat);
      double thatLatRad = Math.toRadians(thatLat);

      double a = Math.pow(Math.sin(dLat / 2), 2)
            + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(latRad)
                  * Math.cos(thatLatRad);
      double c = 2 * Math.asin(Math.sqrt(a));
      return R * c;
   }

   public double[] distFromPointWithDistance(double lat1Deg, double lng1Deg,
         double d, double brngDeg)
   {
      double lat1 = Math.toRadians(lat1Deg);
      double lng1 = Math.toRadians(lng1Deg);
      double brng = Math.toRadians(brngDeg);
      double dRad = d / (6372797.6);

      double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dRad)
            + Math.cos(lat1) * Math.sin(dRad) * Math.cos(brng));
      double lng2 = lng1
            + Math.atan2(Math.sin(brng) * Math.sin(dRad) * Math.cos(lat1),
                  Math.cos(dRad) - Math.sin(lat1) * Math.sin(lat2));

      double[] result = new double[2];
      result[0] = Math.toDegrees(lat2);
      result[1] = Math.toDegrees(lng2);
      return result;
   }

   public void makeInitialGuess(int numTowers, List<GridLocation> grid)
   {
      int count = 0;
      long bestScore = 0;
      ArrayList<GridLocation> bestLocations = new ArrayList<>();
      ArrayList<GridLocation> gridInProgress = (ArrayList<GridLocation>) grid;
      while (count < numTowers)
      {
         Collections.sort(gridInProgress, new Comparator<GridLocation>() {
            public int compare(GridLocation g1, GridLocation g2)
            {
               Long score1 = ((GridLocation) g1).getScore();
               Long score2 = ((GridLocation) g2).getScore();

               return score2.compareTo(score1);

            }
         });
         for (CellPhone pH : gridInProgress.get(0).getPhonesHere())
         {
            System.out.println("TEST" + pH.getId());
         }
         List<CellPhone> phonesServiced = gridInProgress.get(0).getPhonesHere();
         bestLocations.add(gridInProgress.get(0));
         gridInProgress.get(0).setHasTower(true);
         System.out.println("Best score is now " + bestScore + " plus "
               + gridInProgress.get(0).getScore() + " from grid ID "
               + gridInProgress.get(0).getId());
         bestScore = bestScore + gridInProgress.get(0).getScore();
         gridInProgress = recalcScores(gridInProgress, phonesServiced);

         count++;
      }
      for (GridLocation loc : bestLocations)
      {
         setLocationScore(loc);
      }
      debugGrid(bestLocations);
      this.setOptimalScore(bestScore);
   }

   private void setLocationScore(GridLocation loc)
   {
      long score = 0;
      for (CellPhone phone : this.phonesInGrid)
      {
         if (metersBetweenTwoPoints(loc.getLat(), loc.getLng(), phone.getLat(),
               phone.getLng()) <= 9000.00 + phone.getRange())
         {
            loc.setScore(score + phone.getValue());
            loc.addPhonesHere(phone);
         }
      }
   }

   private void debugGrid(ArrayList<GridLocation> bestLocations)
   {
      for (GridLocation loc : bestLocations)
      {
         System.out
               .println("Best Grid Location is at " + loc.getLat() + " lat, "
                     + loc.getLng() + " lng with a score of " + loc.getScore());
      }

   }

   public List<CellPhone> getPhonesInGrid()
   {
      return phonesInGrid;
   }

   public void setPhonesInGrid(List<CellPhone> phonesInGrid)
   {
      this.phonesInGrid = phonesInGrid;
   }

   public long getActualScore()
   {
      return actualScore;
   }

   public void setActualScore(long actualScore)
   {
      this.actualScore = actualScore;
   }

   public long getOptimalScore()
   {
      return optimalScore;
   }

   public void setOptimalScore(long optimalScore)
   {
      this.optimalScore = optimalScore;
   }

   public double getGridSize()
   {
      return gridSize;
   }

   public void setGridSize(double gridSize)
   {
      this.gridSize = gridSize;
   }

   public ArrayList<GridLocation> getGrid()
   {
      return grid;
   }

   public void setGrid(ArrayList<GridLocation> grid)
   {
      this.grid = grid;
   }

   public int getGridOffSet()
   {
      return gridOffSet;
   }

   public void setGridOffSet(int gridOffSet)
   {
      this.gridOffSet = gridOffSet;
   }

}
