package simulator.model;

import org.json.JSONObject;

import exceptions.AddRoadContaminationException;
import exceptions.AddVehicletoRoadException;
import exceptions.SetSpeedException;
import exceptions.VehicleMovetoNextRoadException;

public abstract class SimulatedObject {

	protected String _id;

	SimulatedObject(String id) {
		this._id = id;
	}

	public String getId() {
		return _id;
	}

	@Override
	public String toString() {
		return _id;
	}

	abstract void advance(int time) throws VehicleMovetoNextRoadException, SetSpeedException, AddRoadContaminationException, AddVehicletoRoadException;

	abstract public JSONObject report();
}
