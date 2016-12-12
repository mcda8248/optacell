package org.optaplanner.examples.celltower.persistence;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


import org.optaplanner.examples.celltower.domain.CellPhone;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.TowerGrid;
import org.optaplanner.examples.celltower.domain.TowerSchedule;


public class TowerScheduleGenerator   {


 	public TowerSchedule createTowerSchedule(int phones, int towers, double phoneRange, double topLeftLat, double topLeftLng, double bottomRightLat, double bottomRightLng) {
		TowerSchedule schedule = new TowerSchedule();
		schedule.setPhoneList(createPhoneList(phones));
		schedule.setTowerList(createTowerList(towers));
		schedule.setTowerGrid(createTowerGrid(phoneRange, topLeftLat, topLeftLng, bottomRightLat, bottomRightLng, schedule.getPhoneList(), schedule.getTowerList()));
		schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
		schedule.setPriorityPhones();
		return schedule;
	}

    private TowerGrid createTowerGrid(double range, double topLeftCornerLat, double topLeftCornerLng, double bottomRightCornerLat, double topRightCornerLng, List<CellPhone> phoneList, List<CellTower> towerList) {
		TowerGrid newGrid = new TowerGrid(range, topLeftCornerLat, topLeftCornerLng, bottomRightCornerLat, topRightCornerLng, phoneList, towerList);
		return newGrid;
	}

	private List<CellPhone> createPhoneList(int numPhones) {
        int n = numPhones;
        List<CellPhone> phoneList = new ArrayList<CellPhone>(n);
        for (int i = 0; i < n; i++) {
            CellPhone phone = new CellPhone(4000, (36+Math.random()), (-106+Math.random()), (ThreadLocalRandom.current().nextInt(0, 3 + 1)), (ThreadLocalRandom.current().nextInt(0, 999 + 1)), false);
            phone.setId(i);
            phoneList.add(phone);
        }
        CellPhone pri1 = new CellPhone(4000, (36+Math.random()), (-106+Math.random()), (ThreadLocalRandom.current().nextInt(0, 3 + 1)), (ThreadLocalRandom.current().nextInt(0, 999 + 1)), true);
        CellPhone pri2 = new CellPhone(4000, (36+Math.random()), (-106+Math.random()), (ThreadLocalRandom.current().nextInt(0, 3 + 1)), (ThreadLocalRandom.current().nextInt(0, 999 + 1)), true);
        phoneList.add(pri1);
        phoneList.add(pri2);
        return phoneList;
    }

    private List<CellTower> createTowerList(int numTowers) {
    	int n = numTowers;
        List<CellTower> towerList = new ArrayList<CellTower>(n);
        for (int i = 0; i < n; i++) {
            CellTower tower = new CellTower(12000, 0, 0);
            tower.setId(i);        
            towerList.add(tower);
        }
        towerList.get(0).setFreqType("CDMA");
        towerList.get(1).setFreqType("GSM");
        return towerList;
    }

	public TowerSchedule createSmallTowerSchedule() {
		TowerSchedule schedule = new TowerSchedule();
		schedule.setPhoneList(createSmallPhoneList());
		schedule.setTowerList(createTowerList(2));
		schedule.setTowerGrid(createTowerGrid(4000.00, 37.00, -106.00, 36.00, -105.00, schedule.getPhoneList(), schedule.getTowerList()));
		schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
		schedule.setPriorityPhones();
		return schedule;
	}
	
	public TowerSchedule createMediumTowerSchedule() {
		TowerSchedule schedule = new TowerSchedule();
		schedule.setPhoneList(createMediumPhoneList());
		schedule.setTowerList(createTowerList(5));
		schedule.setTowerGrid(createTowerGrid(4000.00, 33.00, -103.00, 30.00, -100.00, schedule.getPhoneList(), schedule.getTowerList()));
		schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
		schedule.setPriorityPhones();
		return schedule;
	}
	
	public TowerSchedule createLargeTowerSchedule() {
		TowerSchedule schedule = new TowerSchedule();
		schedule.setPhoneList(createLargePhoneList());
		schedule.setTowerList(createTowerList(15));
		schedule.setTowerGrid(createTowerGrid(4000.00, 40.00, -110.00, 30.00, -100.00, schedule.getPhoneList(), schedule.getTowerList()));
		schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
		schedule.setPriorityPhones();
		return schedule;
	}
	
	public TowerSchedule createDemoTowerSchedule() {
		TowerSchedule schedule = new TowerSchedule();
		schedule.setPhoneList(createDemoPhoneList());
		schedule.setTowerList(createTowerList(2));
		schedule.setTowerGrid(createTowerGrid(4000.00, 37.00, -101.00, 36.00, -100.00, schedule.getPhoneList(), schedule.getTowerList()));
		schedule.setOptimalScore(schedule.getTowerGrid().getOptimalScore());
		schedule.setPriorityPhones();
		return schedule;
	}



	private List<CellPhone> createSmallPhoneList() {
		List<CellPhone> phoneList = new ArrayList<CellPhone>(12);
        for (int i = 0; i < 10; i++) {
            CellPhone phone = new CellPhone(4000, (36+Math.random()), (-106+Math.random()), 2, 500, false);
            phone.setId(i);
            phoneList.add(phone);
        }
        CellPhone pri1 = new CellPhone(4000, (36+Math.random()), (-106+Math.random()), 2, 500, true);
        CellPhone pri2 = new CellPhone(4000, (36+Math.random()), (-106+Math.random()), 2, 500, true);
        phoneList.add(pri1);
        phoneList.add(pri2);
        return phoneList;
	}
	
	private List<CellPhone> createMediumPhoneList() {
		List<CellPhone> phoneList = new ArrayList<CellPhone>(80);
        for (int i = 0; i < 80; i++) {
            CellPhone phone = new CellPhone(4000, (ThreadLocalRandom.current().nextDouble(30, 33)), (ThreadLocalRandom.current().nextDouble(-103, -100)), 2, 500, false);
            phone.setId(i);
            phoneList.add(phone);
        }
        CellPhone pri1 = new CellPhone(4000, (ThreadLocalRandom.current().nextDouble(30, 33)), (ThreadLocalRandom.current().nextDouble(-103, -100)), 2, 500, true);
        CellPhone pri2 = new CellPhone(4000, (ThreadLocalRandom.current().nextDouble(30, 33)), (ThreadLocalRandom.current().nextDouble(-103, -100)), 2, 500, true);
        phoneList.add(pri1);
        phoneList.add(pri2);
        return phoneList;
	}
	
	private List<CellPhone> createLargePhoneList() {
		List<CellPhone> phoneList = new ArrayList<CellPhone>(250);
        for (int i = 0; i < 250; i++) {
            CellPhone phone = new CellPhone(4000, (ThreadLocalRandom.current().nextDouble(30, 40)), (ThreadLocalRandom.current().nextDouble(-110, -100)), 2, 500, false);
            phone.setId(i);
            phoneList.add(phone);
        }
        CellPhone pri1 = new CellPhone(4000, (ThreadLocalRandom.current().nextDouble(30, 40)), (ThreadLocalRandom.current().nextDouble(-110, -100)), 2, 500, true);
        CellPhone pri2 = new CellPhone(4000, (ThreadLocalRandom.current().nextDouble(30, 40)), (ThreadLocalRandom.current().nextDouble(-110, -100)), 2, 500, true);
        phoneList.add(pri1);
        phoneList.add(pri2);
        return phoneList;
	}
	
	private List<CellPhone> createDemoPhoneList() {
		List<CellPhone> phoneList = new ArrayList<CellPhone>(12);     
        CellPhone nor1 = new CellPhone(4000, 36.4, -100.8, 2, 500, false);
        CellPhone nor2 = new CellPhone(4000, 36.45, -100.85, 2, 500, false);
        CellPhone nor3 = new CellPhone(4000, 36.5, -100.9, 2, 500, false);
        CellPhone nor4 = new CellPhone(4000, 36.1, -100.5, 2, 500, false);
        CellPhone nor5 = new CellPhone(4000, 36.15, -100.55, 2, 500, false);
        CellPhone nor6 = new CellPhone(4000, 36.2, -100.6, 2, 500, false);
        CellPhone nor7 = new CellPhone(4000, 36.3, -100.3, 2, 500, false);
        CellPhone nor8 = new CellPhone(4000, 36.9, -100.2, 2, 500, false);
        CellPhone nor9 = new CellPhone(4000, 36.8, -100.2, 2, 500, false);
        CellPhone nor10 = new CellPhone(4000, 36.5, -100.1, 2, 500, false);
        CellPhone pri1 = new CellPhone(4000, 36.95, -100.95, 2, 500, true);
        CellPhone pri2 = new CellPhone(4000, 36.775, -100.775, 2, 500, true);
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
}
