package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public RoundRobinStrategyBuilder() {
		super("round_robin_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {		
		int timeSlot = 1;
		if(data.has("timeslot")) timeSlot = data.getInt("timeslot");
		LightSwitchingStrategy r = new RoundRobinStrategy (timeSlot);
		return r;
	}

}
