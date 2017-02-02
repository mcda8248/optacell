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

/**
 * Implementation of an optaplanner MoveListFactory.  Generates all possible moves
 * in a schedule that would move a tower from it's current location to another
 * location within the grid
 */
public class TowerLocationChangeMoveFactory
      implements MoveListFactory<TowerSchedule>
{
   /**
    * Generates a list of possible moves that simply move one tower.
    * <p>
    * Implemented as all towers potentially moving to all locations in the grid
    * <p>
    * TODO - Discuss and potentially change move methodology
    *        Weight the grid locations externally to optaplanner?
    *        
    * @param schedule The tower schedule
    * @return The list of all possible moves
    */
   @Override
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
