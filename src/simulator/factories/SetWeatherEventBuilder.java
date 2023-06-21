package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.SetWeatherEventException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws SetWeatherEventException {
		int time = data.getInt("time");
		List<Pair<String,Weather>> list = new ArrayList<Pair<String, Weather>>();
		JSONArray a = data.getJSONArray("info");
		JSONObject obj = new JSONObject();
		for(int i = 0; i < a.length(); i++) {
			obj = (JSONObject) a.get(i);
			Pair<String,Weather> pair = new Pair<String, Weather>(obj.getString("road"),obj.getEnum(Weather.class, "weather"));
			list.add(pair);
		}
		Event w = new SetWeatherEvent(time, list);
		return w;
	}

}
