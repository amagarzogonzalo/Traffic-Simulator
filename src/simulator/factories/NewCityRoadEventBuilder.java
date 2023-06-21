package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event> {

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		Event c = null;		
		int time = data.getInt("time");
		String id = data.getString("id"), src = data.getString("src"), dest = data.getString("dest");
		int length = data.getInt("length"), co2limit = data.getInt("co2limit"), maxspeed = data.getInt("maxspeed");
		Weather w = data.getEnum(Weather.class, "weather");
		c  = new NewCityRoadEvent(time, id, src, dest, length, co2limit, maxspeed, w);
		return c;
	}
}