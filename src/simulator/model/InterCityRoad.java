package simulator.model;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.RoadParametersException;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather)
			throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}
	
	@Override
	void reduceTotalContamination() {
		int x = 1;
		if(weather_conditions.equals(Weather.SUNNY)) x = 2;
		else if(weather_conditions.equals(Weather.CLOUDY)) x = 3;
		else if(weather_conditions.equals(Weather.RAINY)) x = 10;			
		else if(weather_conditions.equals(Weather.WINDY)) x = 15;
		else if(weather_conditions.equals(Weather.STORM)) x = 20;
		total_contamination = ((int)(((100.0-x)/100.0)*total_contamination));
		
		if(total_contamination < 0) total_contamination = 0;
	}

	@Override
	void updateSpeedLimit() {
		if(total_contamination > contamination_alarm_limit) current_speed_limit = (int) (maximum_speed * 0.5);
		else current_speed_limit = maximum_speed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int speed = current_speed_limit;
		if(weather_conditions.equals(Weather.STORM)) speed = (int) (speed* 0.8);
		return speed;
	}

}
