package org.optaplanner.examples.celltower.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

@PlanningSolution
public class TowerSchedule implements Solution<HardSoftLongScore> {

	private List<GridLocation> locationList;
	private List<CellTower> towerList;
	private List<CellPhone> phoneList;
	private TowerGrid towerGrid;
	private HardSoftLongScore score;
	private long optimalScore;
	private List<CellPhone> priorityPhones;
	private boolean strictPri;

	@ValueRangeProvider(id = "locationRange")
	public List<GridLocation> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<GridLocation> locationList) {
		this.locationList = locationList;
	}
	
	@PlanningEntityCollectionProperty
	public List<CellTower> getTowerList() {
		return towerList;
	}

	public void setTowerList(List<CellTower> towerList) {
		this.towerList = towerList;
	}

	
	public TowerGrid getTowerGrid() {
		return towerGrid;
	}

	public void setTowerGrid(TowerGrid towerGrid) {
		this.towerGrid = towerGrid;
		this.locationList = towerGrid.getGrid();
	}

	public long getOptimalScore() {
		return optimalScore;
	}

	public void setOptimalScore(long optimalScore) {
		this.optimalScore = optimalScore;
	}

	public List<CellPhone> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(List<CellPhone> phoneList) {
		this.phoneList = phoneList;
	}

	



	public List<CellPhone> getPriorityPhones() {
		return priorityPhones;
	}

	public void setPriorityPhones() {
		List<CellPhone> priorityPhones = new ArrayList<>();
		for (CellPhone phone : this.phoneList) {
			if (phone.isPriority()) {
				priorityPhones.add(phone);
			}
		}
		this.priorityPhones = priorityPhones;
	}

	@Override
	public Collection<? extends Object> getProblemFacts() {
		List<Object> facts = new ArrayList<>();
        facts.addAll(locationList);
        // Do not add the planning entity's (processList) because that will be done automatically
        return facts;
	}

	@Override
	public HardSoftLongScore getScore() {
		return this.score;
	}

	@Override
	public void setScore(HardSoftLongScore score) {
		this.score = score;
		
	}

	public boolean isStrictPri() {
		return strictPri;
	}

	public void setStrictPri(boolean strictPri) {
		this.strictPri = strictPri;
	}


}
