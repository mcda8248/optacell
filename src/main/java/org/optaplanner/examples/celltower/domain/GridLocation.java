package org.optaplanner.examples.celltower.domain;

import java.util.List;

public class GridLocation
{
   private int id;
   private double lat;
   private double lng;
   private boolean hasTower;
   private CellTower towerHere;
   private long score;
   private List<CellPhone> phonesHere;
   private List<Integer> priHere;

   public GridLocation(double lat, double lng, long score, int id)
   {
      this.lat = lat;
      this.lng = lng;
      this.score = score;
      this.id = id;
   }

   public GridLocation()
   {

   }

   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

   public List<CellPhone> getPhonesHere()
   {
      return phonesHere;
   }

   public void setPhonesHere(List<CellPhone> phonesHere)
   {
      this.phonesHere = phonesHere;
   }

   public void addPhonesHere(CellPhone phone)
   {
      this.phonesHere.add(phone);
   }

   public double getLat()
   {
      return lat;
   }

   public void removePhone(CellPhone phone)
   {
      phonesHere.remove(phone);
   }

   public void setLat(double lat)
   {
      this.lat = lat;
   }

   public double getLng()
   {
      return lng;
   }

   public void setLng(double lng)
   {
      this.lng = lng;
   }

   public boolean isHasTower()
   {
      return hasTower;
   }

   public void setHasTower(boolean hasTower)
   {
      this.hasTower = hasTower;
   }

   public CellTower getTowerHere()
   {
      return towerHere;
   }

   public void setTowerHere(CellTower towerHere)
   {
      this.towerHere = towerHere;
   }

   public long getScore()
   {
      return score;
   }

   public void setScore(long score)
   {
      this.score = score;
   }

   public List<Integer> getPriHere()
   {
      return priHere;
   }

   public void setPriHere(List<Integer> priHere)
   {
      this.priHere = priHere;
   }

}
