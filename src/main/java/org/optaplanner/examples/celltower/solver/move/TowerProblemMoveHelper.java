package org.optaplanner.examples.celltower.solver.move;

import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.GridLocation;


public class TowerProblemMoveHelper {

    public static void moveCellTower(ScoreDirector scoreDirector, CellTower tower,
            GridLocation toLocation) {
        scoreDirector.beforeVariableChanged(tower, "towerLocation");
        tower.setTowerLocation(toLocation);
        scoreDirector.afterVariableChanged(tower, "towerLocation");
    }

    private TowerProblemMoveHelper() {
    }

}
