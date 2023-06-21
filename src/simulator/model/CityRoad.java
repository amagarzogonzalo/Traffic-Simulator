package simulator.model;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.RoadParametersException;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather)
			throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}
	
	@Override
	void reduceTotalContamination() {
		if(weather_conditions.equals(Weather.STORM) || weather_conditions.equals(Weather.WINDY)) 
			total_contamination -= 10;
		else total_contamination -= 2;
		
		if(total_contamination < 0) total_contamination = 0;
		
	}

	@Override
	void updateSpeedLimit() {
		current_speed_limit = maximum_speed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (int)(((11.0-v.getContamination_class())/11.0)*current_speed_limit);
	}

}
