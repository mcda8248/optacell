package org.optaplanner.examples.celltower.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

public class GridLocationStrengthWeightFactory implements SelectionSorterWeightFactory<TowerSchedule, GridLocation> {

	@Override
	public Comparable createSorterWeight(TowerSchedule solution, GridLocation location) {
		int numPhonesHere = location.getPhonesHere().size();

		return new GridLocationDifficultyWeight(location, numPhonesHere);
	}

	public static class GridLocationDifficultyWeight implements Comparable<GridLocationDifficultyWeight> {

		private final GridLocation location;
		private final int numPhonesHere;

		public GridLocationDifficultyWeight(GridLocation location, int numPhonesHere) {
			this.location = location;
			this.numPhonesHere = numPhonesHere;
		}

		public int compareTo(GridLocationDifficultyWeight other) {
			return new CompareToBuilder().append(other.numPhonesHere, numPhonesHere).toComparison();
		}

	}
}
