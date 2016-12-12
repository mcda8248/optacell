package org.optaplanner.examples.celltower.solver.score;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import org.optaplanner.examples.celltower.domain.CellPhone;
import org.optaplanner.examples.celltower.domain.CellTower;
import org.optaplanner.examples.celltower.domain.TowerSchedule;

public class CellTowerEasyScoreCalc implements EasyScoreCalculator<TowerSchedule> {

	public HardSoftLongScore calculateScore(TowerSchedule locations) {
		long hardScore = 0;
		long softScore = 0;


		softScore = getSoftScore(locations);
		hardScore -= checkIfPrioritiesFilled(locations);
	//hardScore -= checkIfPrioritiesContained(locations);
		//hardScore -= checkIfFreqMatch(locations);
		
		return HardSoftLongScore.valueOf(hardScore, softScore);
	}
	
	private long checkIfFreqMatch(TowerSchedule locations) {
		List<CellPhone> priorityPhones = new ArrayList<CellPhone>();

		for (CellPhone phone : locations.getPriorityPhones()) {
			priorityPhones.add(phone);
		}
		for (Iterator<CellPhone> itr = priorityPhones.iterator(); itr.hasNext();) {
			CellPhone phone = itr.next();
			for (CellTower tower : locations.getTowerList()) {
				if (tower.getPhonesServiced() != null && tower.getPhonesServiced().contains(phone)) {
					if (tower.getFreqType().equals(phone.getFreqType())) {
					itr.remove();
					break;
					}
				}
			}
		}

		return priorityPhones.size();
	}

	private long checkIfPrioritiesFilled(TowerSchedule locations) {
		List<CellPhone> priorityPhones = new ArrayList<CellPhone>();

		for (CellPhone phone : locations.getPriorityPhones()) {
			priorityPhones.add(phone);
		}
		for (Iterator<CellPhone> itr = priorityPhones.iterator(); itr.hasNext();) {
			CellPhone phone = itr.next();
			for (CellTower tower : locations.getTowerList()) {
				if (tower.getPhonesServiced() != null && tower.getPhonesServiced().contains(phone)) {
					itr.remove();
					break;
				}
			}
		}

		return priorityPhones.size();
	}
	
	private long checkIfPrioritiesContained(TowerSchedule locations) {
		List<CellPhone> priorityPhones = new ArrayList<CellPhone>();

		for (CellPhone phone : locations.getPriorityPhones()) {
			priorityPhones.add(phone);
		}
		for (Iterator<CellPhone> itr = priorityPhones.iterator(); itr.hasNext();) {
			CellPhone phone = itr.next();
			for (CellTower tower : locations.getTowerList()) {
				if (tower.isContaining(phone)) {
					itr.remove();
					break;
				}
			}
		}

		return priorityPhones.size();
	}
	
	private long getSoftScore(TowerSchedule locations) {
		long softScore = 0 - locations.getOptimalScore();
		List<CellPhone> phonesAlreadyServiced = new ArrayList<CellPhone>();
		for (CellTower tower : locations.getTowerList()) {
			long checkedScore = 0;
			if (tower.getPhonesServiced() != null) {
				for (CellPhone phone : tower.getPhonesServiced()) {
					if (!phonesAlreadyServiced.contains(phone)) {
						checkedScore = checkedScore + phone.getValue();
						phonesAlreadyServiced.add(phone);
					}
				}
			}
			softScore = softScore + checkedScore;
		}
		return softScore;
	}

}
