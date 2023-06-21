package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event> {
	
	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id"), src = data.getString("src"), dest = data.getString("dest");
		int length = data.getInt("length"), co2limit = data.getInt("co2limit"), maxspeed = data.getInt("maxspeed");
		Weather w = data.getEnum(Weather.class, "weather");
		Event i  = new NewInterCityRoadEvent(time, id, src, dest, length, co2limit, maxspeed, w);
		return i;
	}

}
