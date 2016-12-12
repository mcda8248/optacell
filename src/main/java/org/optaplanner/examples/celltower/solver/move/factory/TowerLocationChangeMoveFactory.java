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
		List<Move> moveList = new ArrayList<Move>();
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

}
