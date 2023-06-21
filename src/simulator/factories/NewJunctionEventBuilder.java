package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.NewSetContClassEventException;
import exceptions.SetWeatherEventException;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
Factory<LightSwitchingStrategy> lssFactory;
Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws JSONException, SetWeatherEventException, NewSetContClassEventException {
		int time = data.getInt("time");
		String id = data.getString("id");
		int x = data.getJSONArray("coor").getInt(0), y = data.getJSONArray("coor").getInt(1);
		JSONObject ls = data.getJSONObject("ls_strategy");
		JSONObject dq = data.getJSONObject("dq_strategy");
		Event j = new NewJunctionEvent(time,id, lssFactory.createInstance(ls), dqsFactory.createInstance(dq), x,y);
		return j;
	}

}
