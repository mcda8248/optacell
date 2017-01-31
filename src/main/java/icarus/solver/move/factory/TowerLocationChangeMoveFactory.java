package icarus.solver.move.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;
import icarus.model.TowerSchedule;
import icarus.solver.move.TowerLocationChangeMove;

public class TowerLocationChangeMoveFactory
      implements MoveListFactory<TowerSchedule>
{

   public List<Move> createMoveList(TowerSchedule schedule)
   {
      List<Move> moveList = new ArrayList<>();
      Collection<GeodeticLocation2D> locationList = schedule.getLocationList();
      for (CellTower tower : schedule.getTowerList())
      {
         for (GeodeticLocation2D location : locationList)
         {
            moveList.add(new TowerLocationChangeMove(tower, location));
         }
      }
      return moveList;
   }
}
