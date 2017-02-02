package icarus.solver.move;

import java.util.Arrays;
import java.util.Collection;

import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;

/**
 * Extension of an Optaplanner AbstractMove.  Represents cell tower moves that
 * swap the location of two towers
 */
public class TowerLocationSwapMove extends AbstractMove
{
   /** A tower that may swap positions with another */
   private CellTower tower;
   /** The other tower swapping positions */
   private CellTower otherTower;

   /**
    * Constructor, sets the two towers that may be swapped
    * @param one The first tower
    * @param two The other tower
    */
   public TowerLocationSwapMove(CellTower one, CellTower two)
   {
      tower = one;
      otherTower = two;
   }

   /**
    * Determines if this move is doable.  A swap move is doable so long as the two towers are
    * not already in the same location
    * @see org.optaplanner.core.impl.heuristic.move.Move#isMoveDoable(org.optaplanner.core.impl.score.director.ScoreDirector)
    * @return True if the move is doable, false if it is not
    */
   @Override
   public boolean isMoveDoable(ScoreDirector scoreDirector)
   {
      return tower.getLocation() != null && !tower.getLocation().equals(otherTower.getLocation());
   }

   /**
    * Creates a move that would undo this move, in this case it is essentially a clone
    * of this move, swapping these two tower back to their original locations
    * @see org.optaplanner.core.impl.heuristic.move.Move#createUndoMove(org.optaplanner.core.impl.score.director.ScoreDirector)
    * @param scoreDirector Optaplanner object that holds it's current working solution
    */
   @Override
   public Move createUndoMove(ScoreDirector scoreDirector)
   {
      return new TowerLocationSwapMove(tower, otherTower);
   }

   /**
    * Returns all planning entities manipulated by this move, namely the
    * two towers involved
    * @see org.optaplanner.core.impl.heuristic.move.Move#getPlanningEntities()
    * @return A collection containing the two towers being swapped
    */
   @Override
   public Collection<? extends Object> getPlanningEntities()
   {
      return Arrays.asList(tower, otherTower);
   }

   /**
    * Returns all plannign values being used by this move, namely the locations
    * of the two towers
    * @see org.optaplanner.core.impl.heuristic.move.Move#getPlanningValues()
    * @return A collection containing the locations of the two towers being swapped
    */
   @Override
   public Collection<? extends Object> getPlanningValues()
   {
      return Arrays.asList(tower.getLocation(),
            otherTower.getLocation());
   }

   /**
    * Used by Optaplanner to swap the position of two towers
    * @see org.optaplanner.core.impl.heuristic.move.AbstractMove#doMoveOnGenuineVariables(org.optaplanner.core.impl.score.director.ScoreDirector)
    */
   @Override
   protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector)
   {
      GeodeticLocation2D oldTowerLocation = tower.getLocation();
      GeodeticLocation2D oldOtherTowerLocation = otherTower.getLocation();
      tower.clearLocation();
      otherTower.clearLocation();
      TowerProblemMoveHelper.moveCellTower(scoreDirector, tower,
            oldTowerLocation);
      TowerProblemMoveHelper.moveCellTower(scoreDirector, otherTower,
            oldOtherTowerLocation);
   }
}