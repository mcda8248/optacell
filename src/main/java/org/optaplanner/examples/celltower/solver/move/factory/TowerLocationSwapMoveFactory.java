package org.optaplanner.examples.celltower.solver.move.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.TowerSchedule;
import org.optaplanner.examples.celltower.solver.move.TowerLocationSwapMove;


public class TowerLocationSwapMoveFactory implements MoveListFactory<TowerSchedule> {

    public List<Move> createMoveList(TowerSchedule assignment) {
        List<CellTower> towerList = assignment.getTowerList();
        List<Move> moveList = new ArrayList<Move>();
        for (ListIterator<CellTower> firstTowerList = towerList.listIterator(); firstTowerList.hasNext();) {
            CellTower tower = firstTowerList.next();
            for (ListIterator<CellTower> otherTowerList = towerList.listIterator(firstTowerList.nextIndex()); otherTowerList.hasNext();) {
                CellTower otherTower = otherTowerList.next();
                moveList.add(new TowerLocationSwapMove(tower, otherTower));
            }
        }
        return moveList;
    }

}