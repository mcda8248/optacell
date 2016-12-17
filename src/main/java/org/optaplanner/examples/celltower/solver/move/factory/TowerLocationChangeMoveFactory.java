package org.optaplanner.examples.celltower.solver.move.factory;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.GridLocation;
import org.optaplanner.examples.celltower.domain.TowerSchedule;
import org.optaplanner.examples.celltower.solver.move.TowerLocationChangeMove;

public class TowerLocationChangeMoveFactory implements MoveListFactory<TowerSchedule> {

	public List<Move> createMoveList(TowerSchedule assignment) {

		if (assignment.isStrictPri()) {
			return createStrictPriMoveList(assignment);
		} else {
			return createNormalMoveList(assignment);
		}
	}

	private List<Move> createNormalMoveList(TowerSchedule assignment) {
		List<Move> moveList = new ArrayList<>();
		List<GridLocation> locationList = assignment.getLocationList();
		for (CellTower tower : assignment.getTowerList()) {
			for (GridLocation location : locationList) {
				if (location.getScore() > 0) {
					moveList.add(new TowerLocationChangeMove(tower, location));
				}

			}
		}
		return moveList;

	}

	private List<Move> createStrictPriMoveList(TowerSchedule assignment) {
		List<Move> moveList = new ArrayList<>();
		List<GridLocation> locationList = assignment.getLocationList();
		List<GridLocation> best = new ArrayList<>();
		for (int i = 1; i < assignment.getTowerList().size() + 1; i++) {
			List<Integer> pri = new ArrayList<>();
			pri.add(i);
			for (GridLocation loc : checkStrictPriority(pri, assignment.getTowerGrid().getGrid())) {
				best.add(loc);
			}
		}

		for (CellTower tower : assignment.getTowerList()) {
			for (GridLocation location : best) {
				if (location.getScore() > 0) {
					moveList.add(new TowerLocationChangeMove(tower, location));
				}

			}
		}
		return moveList;

	}

	private List<GridLocation> checkStrictPriority(List<Integer> pri, List<GridLocation> locations) {

		int nextPri = (pri.get(pri.size() - 1) + 1);
		List<GridLocation> newLocs = new ArrayList<>();

		for (GridLocation loc : locations) {
			int count = 0;
			for (Integer intpri : pri) {
				if (loc.getPriHere().contains(intpri)) {
					count++;
				}
			}
			if (count == pri.size()) {
				newLocs.add(loc);
			}
		}

		if (newLocs.size() > 0) {
			pri.add(nextPri);
			locations = checkStrictPriority(pri, newLocs);
		}
		return locations;

	}
}
