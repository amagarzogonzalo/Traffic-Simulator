package simulator.factories;

import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;
import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxspeed = data.getInt("maxspeed"), co2 = data.getInt("class");
		List<String> it = new SortedArrayList<String>();
		for(int i = 0;i < data.getJSONArray("itinerary").length(); i++) 
			it.add((String) data.getJSONArray("itinerary").get(i));	
		Event v = new NewVehicleEvent(time, id, maxspeed, co2, it);
		return v;
	}

}
