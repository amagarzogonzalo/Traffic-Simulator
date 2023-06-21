package simulator.model;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.AddRoadException;
import exceptions.RoadParametersException;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}
	
	
	@Override
	void execute(RoadMap map) throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException, AddRoadException {
		Junction s = map.getJunction(srcJunc);
		Junction d = map.getJunction(destJunc);
		InterCityRoad intercityroad = new InterCityRoad(id, s, d, maxSpeed, co2Limit, length, weather);
		map.addRoad(intercityroad);
	}
	
	@Override
	public String toString() {
		return "New InterCityRoad '" + id + "'";
	}
	
}