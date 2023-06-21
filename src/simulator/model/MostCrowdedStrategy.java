package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	private int timeSlot;
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		if(roads.isEmpty()) return -1;
		else if(currGreen == -1) {
			List<Vehicle> result = qs.get(0);
			int index = 0;
			for(int i = 0; i < qs.size(); i++) {
				if(qs.get(i).size() > result.size()) {
					result = qs.get(i);
					index = i;
				}
			}
			return index;
		}
		else if ((currTime-lastSwitchingTime) < timeSlot) return currGreen;
		else {
			List<Vehicle> result;
			if((currGreen+1) < qs.size())result = qs.get(currGreen+1);
			else result = qs.get(currGreen);
			int index = 0;
			boolean found = false;
			for(int i = currGreen+1; i < qs.size() && !found; i++) {
				if(qs.get(i).size() > result.size()) {
					result = qs.get(i);
					index = i;
					found = true;
					}
			}
			for(int i = 0; i <= currGreen && !found; i++) {
				if(qs.get(i).size() > result.size()) {
					index = i;
					result = qs.get(i);
					found = true;
				}
			}
			return index;
		}
	}
}
