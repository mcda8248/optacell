package org.optaplanner.examples.celltower.domain;

import java.util.ArrayList;

public class CellPhone {

	private int id;
	final int mainPriority;
	final int subPriority;
	private double range;
	private double lat;
	private double lng;
	private long value;
	private ArrayList<double[]> circlePoints = new ArrayList<>();
	private boolean isPriority;
	private String freqType;

	public CellPhone(double range, double lat, double lng, int mainPri, int subPri, boolean isPriority) {
		this.range = range;
		this.lat = lat;
		this.lng = lng;
		this.mainPriority = mainPri;
		this.subPriority = subPri;
		this.value = ((3 + 1 - mainPri) ^ 2 * 1000 + 999 + 1 - subPri) * 1000;
		for (int i = 0; i <= 360; i = i + 45) {
			double[] array = new double[2];
			array = distFromPointWithDistance(lat, lng, range, i);
			this.circlePoints.add(array);
		}
		this.isPriority = isPriority;
	}
	
	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}



	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
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

	public int getMainPriority() {
		return mainPriority;
	}

	public int getSubPriority() {
		return subPriority;
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

	public boolean isPriority() {
		return isPriority;
	}

	public void setPriority(boolean isPriority) {
		this.isPriority = isPriority;
	}

}
