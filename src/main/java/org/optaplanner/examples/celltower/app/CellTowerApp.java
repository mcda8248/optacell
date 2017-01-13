package org.optaplanner.examples.celltower.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.celltower.domain.CellPhone;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.GridLocation;
import org.optaplanner.examples.celltower.domain.TowerSchedule;
import org.optaplanner.examples.celltower.persistence.TowerScheduleGenerator;

public class CellTowerApp {

	public static void main(String[] args) {

		SolverFactory<TowerSchedule> solverFactory = SolverFactory
				.createFromXmlResource("org/optaplanner/examples/celltower/solver/CellTowerSolverConfig.xml");
		
		if (args.length > 0) {
			switch (args[0]) {
			case "s": solveSmallArea(solverFactory);
			break;
			case "l": solveLargeArea(solverFactory);
			break;
			case "d": solveDemoArea(solverFactory);
			break;
			}
		} else {
		solveForPhonesInArea(solverFactory, 10, 2, 4000.00, 37.00, -106.00, 36.00, -105.00);
	}
		
		
	}

	private static void solveSmallArea(SolverFactory<TowerSchedule> solverFactory) {
		Solver solver = solverFactory.buildSolver();
		TowerSchedule unsolvedTowerSchedule = new TowerScheduleGenerator().createSmallTowerSchedule();
		TowerSchedule solvedTowerSchedule = null;
		solver.solve(unsolvedTowerSchedule);
		solvedTowerSchedule = (TowerSchedule) solver.getBestSolution();
		printTowerScheduleinGeoJSON(solvedTowerSchedule);
	}
	
	private static void solveLargeArea(SolverFactory<TowerSchedule> solverFactory) {
		Solver solver = solverFactory.buildSolver();
		TowerSchedule unsolvedTowerSchedule = new TowerScheduleGenerator().createMediumTowerSchedule();
		TowerSchedule solvedTowerSchedule = null;
		solver.solve(unsolvedTowerSchedule);
		solvedTowerSchedule = (TowerSchedule) solver.getBestSolution();
		printTowerScheduleinGeoJSON(solvedTowerSchedule);
	}

	
	private static void solveDemoArea(SolverFactory<TowerSchedule> solverFactory) {
		Solver solver = solverFactory.buildSolver();
		TowerSchedule unsolvedTowerSchedule = new TowerScheduleGenerator().createDemoTowerSchedule();
		TowerSchedule solvedTowerSchedule = null;
		solver.solve(unsolvedTowerSchedule);
		solvedTowerSchedule = (TowerSchedule) solver.getBestSolution();
		printTowerScheduleinGeoJSON(solvedTowerSchedule);
	}

	private static void solveForPhonesInArea(SolverFactory solverFactory, int phones, int towers, double phoneRange,
			double topLeftLat, double topLeftLng, double bottomRightLat, double bottomRightLng) {

		Solver solver = solverFactory.buildSolver();
		TowerSchedule unsolvedTowerSchedule = new TowerScheduleGenerator().createTowerSchedule(phones, towers,
				phoneRange, topLeftLat, topLeftLng, bottomRightLat, bottomRightLng);

		TowerSchedule solvedTowerSchedule = null;
		solver.solve(unsolvedTowerSchedule);
		solvedTowerSchedule = (TowerSchedule) solver.getBestSolution();
		printTowerScheduleinGeoJSON(solvedTowerSchedule);
	}

	private static void printTowerScheduleinGeoJSON(TowerSchedule solvedTowerSchedule) {
		System.out.println(solvedTowerSchedule.getTowerList().size());

		List<CellTower> initialGuess = solvedTowerSchedule.getTowerList();
		for (CellTower tower : initialGuess) {
			tower.setCirclePoints();
		}
		JSONObject featureCollection = new JSONObject();
		try {
			featureCollection.put("type", "FeatureCollection");
			JSONArray featureList = new JSONArray();
			List<CellPhone> phoneList = solvedTowerSchedule.getPhoneList();
			List<List<GridLocation>> latLines = solvedTowerSchedule.getTowerGrid().getLatLines();
			int count = 0;
			for (CellPhone phone : phoneList) {

				JSONObject polygon = new JSONObject();
				polygon.put("type", "Polygon");
				double[][][] polyPoints = new double[1][9][2];
				int phoneCircleCount = 0;
				for (int i = 0; i < 8; i++) {
					polyPoints[0][i] = phone.getCirclePoints().get(phoneCircleCount);
					phoneCircleCount++;
				}
				polyPoints[0][8] = phone.getCirclePoints().get(0);
				polygon.put("coordinates", polyPoints);
				JSONObject feature = new JSONObject();
				feature.put("geometry", polygon);
				JSONObject properties = new JSONObject();
				properties.put("id", phone.getId());
				if (phone.isPriority()) {
					properties.put("fill", "#FF0000");
				}
				feature.put("type", "Feature");
				feature.put("properties", properties);
				featureList.put(feature);
			}
			for (List<GridLocation> latLine : latLines) {
				JSONObject lineString = new JSONObject();
				lineString.put("type", "LineString");

				double[][] gridArray = new double[latLine.size()][2];
				for (int i = 0; i < latLine.size(); i++) {
					gridArray[i][0] = latLine.get(i).getLng();
					gridArray[i][1] = latLine.get(i).getLat();
				}

				JSONArray coord = new JSONArray(gridArray);

				lineString.put("coordinates", coord);
				JSONObject feature = new JSONObject();
				feature.put("geometry", lineString);
				JSONObject properties = new JSONObject();
				feature.put("type", "Feature");
				feature.put("properties", properties);
				// System.out.println(feature.toString());
				featureList.put(feature);
			}
			for (CellTower placedTower : initialGuess) {
				JSONObject polygon = new JSONObject();
				polygon.put("type", "Polygon");
				double[][][] polyPoints = new double[1][9][2];
				int towerCircleCount = 0;
				for (int i = 0; i < 8; i++) {
					polyPoints[0][i] = placedTower.getCirclePoints().get(towerCircleCount);
					towerCircleCount++;
				}
				polyPoints[0][8] = placedTower.getCirclePoints().get(0);
				polygon.put("coordinates", polyPoints);

				JSONObject feature = new JSONObject();
				feature.put("geometry", polygon);
				JSONObject properties = new JSONObject();
				properties.put("fill", "#32CD32");
				feature.put("type", "Feature");
				feature.put("properties", properties);
				featureList.put(feature);
			}
			featureCollection.put("features", featureList);
		} catch (JSONException e) {
			System.out.println("error");
		}

		System.out.println(featureCollection.toString());

		for (CellTower placed : initialGuess) {
			System.out.println("This tower is at " + placed.getLat() + " lat and " + placed.getLng()
					+ " lng. This tower has a total score of " + placed.getScore());

		}

	}
}
