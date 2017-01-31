package icarus.generator;

import java.util.ArrayList;
import java.util.List;

import icarus.model.CellPhone;
import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;
import icarus.util.CellTowerUtil;

public class TowerScheduleGenerator
{
   public TowerSchedule createTowerSchedule(int phones,
         int towers,
         double phoneRange,
         GeodeticLocation2D topLeft,
         GeodeticLocation2D bottomRight,
         double latGranularity,
         double lonGranularity)
   {
      TowerSchedule schedule = new TowerSchedule();
      schedule.setPhoneList(createPhoneList(phones, phoneRange, topLeft, bottomRight));
      schedule.setTowerList(createTowerList(towers));
      schedule.setLocationList(CellTowerUtil.buildGrid(topLeft, bottomRight, latGranularity, lonGranularity));
      return schedule;
   }

   private List<CellPhone> createPhoneList(int numPhones,
         double phoneRange,
         GeodeticLocation2D topLeft,
         GeodeticLocation2D bottomRight)
   {
      int n = numPhones;
      List<CellPhone> phoneList = new ArrayList<>(n);
      //For now, testing just uses the id as the priority
      int id = 1;
      double latScale = Math.abs(bottomRight.getLatitude() - topLeft.getLatitude());
      double lonScale = Math.abs(bottomRight.getLongitude() - topLeft.getLongitude());
      for (int i = 0; i < n; i++)
      {
         double thisLat = topLeft.getLatitude() + Math.random() * latScale;
         double thisLon = topLeft.getLongitude() + Math.random() * lonScale;
         CellPhone phone = new CellPhone(id,
               phoneRange,
               new GeodeticLocation2D(thisLat, thisLon),
               null,
               id);
         phoneList.add(phone);
         id++;
      }
      return phoneList;
   }

   private List<CellTower> createTowerList(int numTowers)
   {
      int n = numTowers;
      List<CellTower> towerList = new ArrayList<>(n);
      for (int i = 0; i < n; i++)
      {
         CellTower tower = new CellTower(i, 12000);
         towerList.add(tower);
      }
      towerList.get(0).addFreqType("CDMA");
      towerList.get(1).addFreqType("GSM");
      return towerList;
   }

   /*
   public TowerSchedule createSmallTowerSchedule()
   {
      TowerSchedule schedule = new TowerSchedule();
      schedule.setPhoneList(createSmallPhoneList());
      schedule.setTowerList(createTowerList(2));
      schedule.setTowerGrid(createTowerGrid(4000.00, 37.00, -106.00, 36.00,
            -105.00, schedule.getPhoneList(), schedule.getTowerList()));
      schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
      schedule.setPriorityPhones();
      return schedule;
   }

   public TowerSchedule createMediumTowerSchedule()
   {
      TowerSchedule schedule = new TowerSchedule();
      schedule.setPhoneList(createMediumPhoneList());
      schedule.setTowerList(createTowerList(5));
      schedule.setTowerGrid(createTowerGrid(4000.00, 33.00, -103.00, 30.00,
            -100.00, schedule.getPhoneList(), schedule.getTowerList()));
      schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
      schedule.setPriorityPhones();
      return schedule;
   }

   public TowerSchedule createLargeTowerSchedule()
   {
      TowerSchedule schedule = new TowerSchedule();
      schedule.setPhoneList(createLargePhoneList());
      schedule.setTowerList(createTowerList(15));
      schedule.setTowerGrid(createTowerGrid(4000.00, 40.00, -110.00, 30.00,
            -100.00, schedule.getPhoneList(), schedule.getTowerList()));
      schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
      schedule.setPriorityPhones();
      return schedule;
   }

   public TowerSchedule createDemoTowerSchedule()
   {
      TowerSchedule schedule = new TowerSchedule();
      schedule.setPhoneList(createDemoPhoneList());
      schedule.setTowerList(createTowerList(2));
      schedule.setTowerGrid(createTowerGrid(4000.00, 37.00, -101.00, 36.00,
            -100.00, schedule.getPhoneList(), schedule.getTowerList()));
      schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
      schedule.setPriorityPhones();
      return schedule;
   }

   private List<CellPhone> createSmallPhoneList()
   {
      List<CellPhone> phoneList = new ArrayList<>(12);
      for (int i = 0; i < 10; i++)
      {
         CellPhone phone = new CellPhone(4000, (36 + Math.random()),
               (-106 + Math.random()), 500, false);
         phone.setId(i);
         phoneList.add(phone);
      }
      CellPhone pri1 = new CellPhone(4000, (36 + Math.random()),
            (-106 + Math.random()), 500, true);
      CellPhone pri2 = new CellPhone(4000, (36 + Math.random()),
            (-106 + Math.random()), 500, true);
      phoneList.add(pri1);
      phoneList.add(pri2);
      return phoneList;
   }

   private List<CellPhone> createMediumPhoneList()
   {
      List<CellPhone> phoneList = new ArrayList<>(80);
      for (int i = 0; i < 80; i++)
      {
         CellPhone phone = new CellPhone(4000,
               (ThreadLocalRandom.current().nextDouble(30, 33)),
               (ThreadLocalRandom.current().nextDouble(-103, -100)), 500,
               false);
         phone.setId(i);
         phoneList.add(phone);
      }
      CellPhone pri1 = new CellPhone(4000,
            (ThreadLocalRandom.current().nextDouble(30, 33)),
            (ThreadLocalRandom.current().nextDouble(-103, -100)), 500, true);
      CellPhone pri2 = new CellPhone(4000,
            (ThreadLocalRandom.current().nextDouble(30, 33)),
            (ThreadLocalRandom.current().nextDouble(-103, -100)), 500, true);
      phoneList.add(pri1);
      phoneList.add(pri2);
      return phoneList;
   }

   private List<CellPhone> createLargePhoneList()
   {
      List<CellPhone> phoneList = new ArrayList<>(250);
      for (int i = 0; i < 250; i++)
      {
         CellPhone phone = new CellPhone(4000,
               (ThreadLocalRandom.current().nextDouble(30, 40)),
               (ThreadLocalRandom.current().nextDouble(-110, -100)), 500,
               false);
         phone.setId(i);
         phoneList.add(phone);
      }
      CellPhone pri1 = new CellPhone(4000,
            (ThreadLocalRandom.current().nextDouble(30, 40)),
            (ThreadLocalRandom.current().nextDouble(-110, -100)), 500, true);
      CellPhone pri2 = new CellPhone(4000,
            (ThreadLocalRandom.current().nextDouble(30, 40)),
            (ThreadLocalRandom.current().nextDouble(-110, -100)), 500, true);
      phoneList.add(pri1);
      phoneList.add(pri2);
      return phoneList;
   }

   private List<CellPhone> createDemoPhoneList()
   {
      List<CellPhone> phoneList = new ArrayList<>(12);
      CellPhone nor1 = new CellPhone(4000, 36.4, -100.8, 500, false);
      CellPhone nor2 = new CellPhone(4000, 36.45, -100.85, 500, false);
      CellPhone nor3 = new CellPhone(4000, 36.5, -100.9, 500, false);
      CellPhone nor4 = new CellPhone(4000, 36.1, -100.5, 500, false);
      CellPhone nor5 = new CellPhone(4000, 36.15, -100.55, 500, false);
      CellPhone nor6 = new CellPhone(4000, 36.2, -100.6, 500, false);
      CellPhone nor7 = new CellPhone(4000, 36.3, -100.3, 500, false);
      CellPhone nor8 = new CellPhone(4000, 36.9, -100.2, 500, false);
      CellPhone nor9 = new CellPhone(4000, 36.8, -100.2, 500, false);
      CellPhone nor10 = new CellPhone(4000, 36.5, -100.1, 500, false);
      CellPhone pri1 = new CellPhone(4000, 36.95, -100.95, 500, true);
      CellPhone pri2 = new CellPhone(4000, 36.775, -100.775, 500, true);
      pri1.setFreqType("CDMA");
      pri2.setFreqType("GSM");
      nor1.setId(1);
      nor2.setId(2);
      nor3.setId(3);
      nor4.setId(4);
      nor5.setId(5);
      nor6.setId(6);
      nor7.setId(7);
      nor8.setId(8);
      nor9.setId(9);
      nor10.setId(10);
      pri1.setId(11);
      pri2.setId(12);
      phoneList.add(nor1);
      phoneList.add(nor2);
      phoneList.add(nor3);
      phoneList.add(nor4);
      phoneList.add(nor5);
      phoneList.add(nor6);
      phoneList.add(nor7);
      phoneList.add(nor8);
      phoneList.add(nor9);
      phoneList.add(nor10);
      phoneList.add(pri1);
      phoneList.add(pri2);
      return phoneList;
   }
   
   */
}
