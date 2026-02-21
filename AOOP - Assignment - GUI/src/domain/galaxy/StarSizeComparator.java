package domain.galaxy;

import java.util.Comparator;

public class StarSizeComparator implements Comparator<Star> {

	@Override
	public int compare(Star o1, Star o2) {
		if (o1.getType() != o2.getType()) {
			return o1.getType().compareTo(o2.getType());
		}
		else if (o1.getTemperatureSequence() != o2.getTemperatureSequence()) {
			return o1.getTemperatureSequence() - o2.getTemperatureSequence();
		}
		else if (o1.getLuminosity() != o2.getLuminosity()) {
			return o1.getLuminosity().compareTo(o2.getLuminosity());
		}
		else if (o1.getCoordinate().getDistance() != o2.getCoordinate().getDistance()) {
			return (int) (o1.getCoordinate().getDistance() - o2.getCoordinate().getDistance());
		}
		else {
			return o1.getDesignation().compareTo(o2.getDesignation());
		}
	}
}
