package org.optaplanner.examples.celltower.domain;

import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class GridLocationNearbyDistanceMeter implements NearbyDistanceMeter<CellTower, GridLocation> { 

    public double getNearbyDistance(CellTower origin, GridLocation destination) {
        return origin.getDistanceTo(destination);
    }

}

