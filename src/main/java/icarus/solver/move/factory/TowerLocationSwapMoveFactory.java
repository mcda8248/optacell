package icarus.solver.move.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import icarus.model.CellTower;
import icarus.model.TowerSchedule;
import icarus.solver.move.TowerLocationSwapMove;

/**
 *  Optaplanner MoveListFactory implementation that generates all moves that
 *  involve swapping two cell towers 
 */
public class TowerLocationSwapMoveFactory
      implements MoveListFactory<TowerSchedule>
{
   /**
    * Generates a list of all tower moves that involve swapping the location
    * of two towers
    * @param schedule The current tower schedule
    * @return The list of all tower swapping moves
    */
   @Override
   public List<Move> createMoveList(TowerSchedule schedule)
   {
      List<CellTower> towerList = schedule.getTowerList();
      List<Move> moveList = new ArrayList<Move>();
      for (ListIterator<CellTower> firstTowerList = towerList
            .listIterator(); firstTowerList.hasNext();)
      {
         CellTower tower = firstTowerList.next();
         for (ListIterator<CellTower> otherTowerList = towerList.listIterator(
               firstTowerList.nextIndex()); otherTowerList.hasNext();)
         {
            CellTower otherTower = otherTowerList.next();
            moveList.add(new TowerLocationSwapMove(tower, otherTower));
         }
      }
      return moveList;
   }

}