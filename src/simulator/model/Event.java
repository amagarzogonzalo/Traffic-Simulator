package simulator.model;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.AddRoadException;
import exceptions.AddVehicleException;
import exceptions.JunctionParametersException;
import exceptions.RoadParametersException;
import exceptions.SetContaminationException;
import exceptions.SetWeatherRoadException;
import exceptions.VehicleParametersException;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		else
			_time = time;
	}

	public int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event o) {
		// TODO complete
		return 0;
	}
	public abstract String toString();

	abstract void execute(RoadMap map) throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException, AddRoadException, 
	JunctionParametersException, SetContaminationException, VehicleParametersException, AddVehicleException, SetWeatherRoadException;
}
