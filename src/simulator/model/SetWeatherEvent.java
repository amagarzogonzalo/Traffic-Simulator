package simulator.model;

import java.util.List;
import exceptions.SetWeatherEventException;
import exceptions.SetWeatherRoadException;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws SetWeatherEventException {
		super(time);
		if(ws.equals(null)) throw new SetWeatherEventException();
		else this.ws = ws;
	}
	
	@Override
	void execute(RoadMap map) throws SetWeatherRoadException {
		for(Pair<String,Weather> w: ws) 
			map.getRoad(w.getFirst()).setWeather(w.getSecond());
	}

	@Override
	public String toString() {
		return "New Weather " + ws.get(0).getFirst().toString() + ", " + ws.get(0).getSecond().toString();
	}

}
