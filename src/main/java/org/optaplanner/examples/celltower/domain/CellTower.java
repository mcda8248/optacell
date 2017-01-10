package org.optaplanner.examples.celltower.domain;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.celltower.solver.move.factory.GridLocationStrengthWeightFactory;

@PlanningEntity
public class CellTower {
	private List<CellPhone> phonesServiced;
	private int id;
	private double range;
	private double lat;
	private long score;
	private double lng;
	private GridLocation towerLocation;
	private ArrayList<double[]> circlePoints = new ArrayList<>(); 
	private String freqType;
	
	public CellTower() {
		
	}
	
	public CellTower(double range, double lat, double lng) {
		this.range = range;
		this.lat = lat;
		this.lng = lng;
}
	

	
	public double metersBetween(double otherLat, double otherLng) {
		final double R = 6372800; //Earth Radius in Kilometers
        double dLat = Math.toRadians(this.lat - otherLat);
        double dLon = Math.toRadians(this.lng - otherLng);
        double latRad = Math.toRadians(this.lat);
        double otherLatRad= Math.toRadians(otherLat);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(latRad) * Math.cos(otherLatRad);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
	
	public void setCirclePoints() {
		for (int i = 0; i<=360; i=i+45) {
			double[] array = new double[2];
			array = distFromPointWithDistance(this.lat, this.lng, this.range, i);
			this.circlePoints.add(array);
		}
	}
	
	public boolean isOverlapping(CellPhone phone) {
		if (metersBetween(phone.getLat(), phone.getLng()) <= (phone.getRange()+this.range)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isContaining(CellPhone phone) {
		if (metersBetween(phone.getLat(), phone.getLng()) <= (this.range-phone.getRange())) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public List<CellPhone> getPhonesServiced() {
		return phonesServiced;
	}
	

	public void setPhonesServiced(List<CellPhone> phonesServiced) {
		this.phonesServiced = phonesServiced;
	}
	
	@PlanningVariable(valueRangeProviderRefs = {"locationRange"} , strengthWeightFactoryClass = GridLocationStrengthWeightFactory.class)
	public GridLocation getTowerLocation() {
		return towerLocation;
	}

	public void setTowerLocation(GridLocation towerLocation) {
		this.towerLocation = towerLocation;
		if (towerLocation != null) {
		this.score = towerLocation.getScore();
		this.lat = towerLocation.getLat();
		this.lng = towerLocation.getLng();
		this.setPhonesServiced(towerLocation.getPhonesHere());
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public ArrayList<double[]> getCirclePoints() {
		return circlePoints;
	}

	public void setCirclePoints(ArrayList<double[]> circlePoints) {
		this.circlePoints = circlePoints;
	}

	
	public double[] distFromPointWithDistance(double lat1Deg, double lng1Deg, double d, double brngDeg) {
		double lat1 = Math.toRadians(lat1Deg);
		double lng1 = Math.toRadians(lng1Deg);
		double brng = Math.toRadians(brngDeg);
		double dRad = d / (6372797.6);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dRad) + Math.cos(lat1) * Math.sin(dRad) * Math.cos(brng));
		double lng2 = lng1 + Math.atan2(Math.sin(brng) * Math.sin(dRad) * Math.cos(lat1),
				Math.cos(dRad) - Math.sin(lat1) * Math.sin(lat2));

		double[] result = new double[2];
		result[1] = Math.toDegrees(lat2);
		result[0] = Math.toDegrees(lng2);
		return result;
	}

	public void clearLocation() {
		this.towerLocation = null;
		
	}

	public void clearPhonesServiced() {
		this.phonesServiced = null;
		
	}

}
