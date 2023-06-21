package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.NewSetContClassEventException;
import exceptions.SetWeatherEventException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) throws SetWeatherEventException, NewSetContClassEventException {
		int time = data.getInt("time"); 
		List<Pair<String,Integer>> list = new ArrayList<Pair<String, Integer>>();
		JSONArray a = data.getJSONArray("info");
		for(int i = 0; i < a.length(); i++) {
			JSONObject obj = (JSONObject) a.get(i);
			Pair<String, Integer> pair = new Pair<String, Integer>(obj.getString("vehicle"),obj.getInt("class"));
			list.add(pair);
		}
		Event c = new NewSetContClassEvent(time, list);
		return c;
	}
}
