package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.AddRoadContaminationException;
import exceptions.AddRoadException;
import exceptions.AddVehicleException;
import exceptions.AddVehicletoRoadException;
import exceptions.ControllerParametersException;
import exceptions.JunctionParametersException;
import exceptions.LoadEventsControllerException;
import exceptions.NewSetContClassEventException;
import exceptions.RoadParametersException;
import exceptions.SetContaminationException;
import exceptions.SetSpeedException;
import exceptions.SetWeatherEventException;
import exceptions.SetWeatherRoadException;
import exceptions.VehicleMovetoNextRoadException;
import exceptions.VehicleParametersException;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;
import simulator.model.Vehicle;

public class Controller {
	private TrafficSimulator traffic_simulator;
	private Factory<Event> events_factory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws ControllerParametersException{
		if(sim.equals(null) || eventsFactory.equals(null)) throw new ControllerParametersException();
		else {
			traffic_simulator = sim;
			events_factory = eventsFactory;
		}
	}
	
	public void loadEvents(InputStream in) throws LoadEventsControllerException, JSONException, SetWeatherEventException, NewSetContClassEventException {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if(jo.equals(null) || !jo.has("events")) throw new LoadEventsControllerException();
		else {
			JSONArray arr = new JSONArray();
			arr = jo.getJSONArray("events");
			for(int i = 0;i < arr.length(); i++) {
				try {
					Event e = events_factory.createInstance(arr.getJSONObject(i));
					if(!e.equals(null))traffic_simulator.addEvent(e);
				} catch(NullPointerException ex) {}
			}
		}
	}
	
	public void run(int n, OutputStream out) throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException,AddRoadException,JunctionParametersException, SetContaminationException,
	VehicleParametersException, AddVehicleException, SetWeatherRoadException, VehicleMovetoNextRoadException, SetSpeedException, AddRoadContaminationException, AddVehicletoRoadException {
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		for(int i = 0; i < n; i++) {
			traffic_simulator.advance();
			a.put(traffic_simulator.report());
		}
		o.put("states",a);
		PrintStream p = new PrintStream(out);
		p.print(o);
	}
	
	// Run GUI
	public void run(int n) throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException,AddRoadException,JunctionParametersException, SetContaminationException,
	VehicleParametersException, AddVehicleException, SetWeatherRoadException, VehicleMovetoNextRoadException, SetSpeedException, AddRoadContaminationException, AddVehicletoRoadException {
		for(int i = 0; i < n; i++) {
			traffic_simulator.advance();
		}
	}
	
	
	public void reset() {
		traffic_simulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		traffic_simulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		traffic_simulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		traffic_simulator.addEvent(e);
	}
	
	public List<Vehicle> getList_vehicles(){	
		return traffic_simulator.getList_vehicles();
	}
	
	public List<Road> getList_roads(){
		return traffic_simulator.getList_roads();
	}
	
	public List<Junction> getList_junctions(){
		return traffic_simulator.getList_junctions();
	}
	
	public int getTime() {
		return traffic_simulator.getTime();
	}
}


