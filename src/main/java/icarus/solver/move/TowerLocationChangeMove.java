package icarus.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import icarus.model.CellTower;
import icarus.model.GeodeticLocation2D;

/**
 * Extension of an Optaplanner AbstractMove.  Represents a possible move of a cell tower to
 * another location within the grid
 */
public class TowerLocationChangeMove extends AbstractMove
{
   /** The tower to be moved */
   private CellTower tower;
   /** The location the tower may be moved to */
   private GeodeticLocation2D toLocation;

   /**
    * Constructor, takes a tower and a location and sets them in this instance
    * @param t The tower to set
    * @param loc The location to set
    */
   public TowerLocationChangeMove(CellTower t, GeodeticLocation2D loc)
   {
      tower = t;
      toLocation = loc;
   }

   /**
    * Determines if the move represented by this object is doable.  The only
    * reason it fails at this time is if the location being moved to is already
    * where the tower is.
    * @see org.optaplanner.core.impl.heuristic.move.Move#isMoveDoable(org.optaplanner.core.impl.score.director.ScoreDirector)
    * @param scoreDirector Optaplanner object that holds the current working solution
    * @return True if the move is doable, false if it is not
    * 
    * TODO - Investigate using the scoreDirector to better determine if the move is worth doing?
    */
   @Override
   public boolean isMoveDoable(ScoreDirector scoreDirector)
   {
      return tower.getLocation() != null && !tower.getLocation().equals(toLocation);
   }

   /**
    * Creates an undo of a move, which is another move that puts the tower back
    * in it's original location
    * @param scoreDirector Optaplanner object that holds the current working solution
    * @return A move that undoes this move
    */
   @Override
   public Move createUndoMove(ScoreDirector scoreDirector)
   {
      return new TowerLocationChangeMove(tower, tower.getLocation());
   }

   /**
    * Returns the planning entities that are being manipulated by this move,
    * in this case, the tower being moved
    * @see org.optaplanner.core.impl.heuristic.move.Move#getPlanningEntities()
    * @return The tower being moved
    */
   @Override
   public Collection<? extends Object> getPlanningEntities()
   {
      return Collections.singletonList(tower);
   }

   /**
    * Returns the planning values that are being used by this move, in this case
    * the location being moved to
    * @see org.optaplanner.core.impl.heuristic.move.Move#getPlanningValues()
    * @return The location being moved to
    */
   @Override
   public Collection<? extends Object> getPlanningValues()
   {
      return Collections.singletonList(toLocation);
   }

   /**
    * Returns a String representation of this move, simply the tower id to
    * the location
    * @see java.lang.Object#toString()
    * @return A string representatin of this move
    */
   @Override
   public String toString()
   {
      return tower + " => " + toLocation;
   }

   /**
    * Used by Optaplanner to move a cell tower
    * @see org.optaplanner.core.impl.heuristic.move.AbstractMove#doMoveOnGenuineVariables(org.optaplanner.core.impl.score.director.ScoreDirector)
    * @param scoreDirector Optaplanner object that holds it's current working solution
    */
   @Override
   protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector)
   {
      tower.clearLocation();
      TowerProblemMoveHelper.moveCellTower(scoreDirector, tower, toLocation);
   }
}