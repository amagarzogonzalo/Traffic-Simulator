package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.AddRoadContaminationException;
import exceptions.AddRoadException;
import exceptions.AddVehicleException;
import exceptions.AddVehicletoRoadException;
import exceptions.JunctionParametersException;
import exceptions.RoadParametersException;
import exceptions.SetContaminationException;
import exceptions.SetSpeedException;
import exceptions.SetWeatherRoadException;
import exceptions.VehicleMovetoNextRoadException;
import exceptions.VehicleParametersException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	private RoadMap roadmap;
	private List<Event> listEvents;
	private int time;
	private List<TrafficSimObserver> observers;
	
	public TrafficSimulator() {
		time = 0;
		listEvents = new SortedArrayList<Event>();
		List<Junction> list_j = new ArrayList<Junction>(); 
		List<Road> list_r = new ArrayList<Road>();
		List <Vehicle> list_v = new ArrayList<Vehicle>();
		Map <String,Junction> junctions_m = new HashMap <String, Junction>();
		Map <String,Road> roads_m = new HashMap<String, Road>();
		Map <String,Vehicle> vehicles_m = new HashMap <String, Vehicle>();
		roadmap = new RoadMap(list_j, list_r, list_v, junctions_m, roads_m, vehicles_m);
		observers = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		listEvents.add(e);
		for(TrafficSimObserver o: observers) 
			o.onEventAdded(roadmap, listEvents, e, time);
	}
	
	public void advance() throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException, 
	AddRoadException, JunctionParametersException, SetContaminationException, VehicleParametersException, AddVehicleException, 
	SetWeatherRoadException, VehicleMovetoNextRoadException, SetSpeedException, AddRoadContaminationException, AddVehicletoRoadException {
		try {
			time++;
			for(TrafficSimObserver o: observers) 
				o.onAdvanceStart(roadmap, listEvents, time);
			for(int i = 0;i < listEvents.size();i++) {
				if(listEvents.get(i)._time == time) {
					listEvents.get(i).execute(roadmap);	
				}
			}
			
			for(int i = 0;i < listEvents.size();i++) 
				if(listEvents.get(i)._time == time) 
					listEvents.remove(i);
			roadmap.advance(time);
	
			for(TrafficSimObserver o: observers) 
				o.onAdvanceEnd(roadmap, listEvents, time);
		} catch(Exception e){
			for(TrafficSimObserver o: observers) 
				o.onError(e.getMessage());	
		}
	}

	public void reset() {
		time = 0;
		listEvents.removeAll(listEvents);
		roadmap.reset();
		for(TrafficSimObserver o: observers) 
			o.onReset(roadmap, listEvents, time);
		
	}
	
	public JSONObject report() {
		JSONObject obj = new JSONObject ();
		obj.put("time", time);	
		obj.put("state", roadmap.report());
		return obj;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		for(TrafficSimObserver ob: observers) 
			ob.onRegister(roadmap, listEvents, time);
		observers.add(o);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);
	}
	
	public List<Vehicle> getList_vehicles(){
		return roadmap.getList_vehicles();
	}
	
	public List<Road> getList_roads(){
		return roadmap.getList_roads();
	}
	
	public List<Junction> getList_junctions(){
		return roadmap.getList_junctions();
	}

	public int getTime() {
		return time;
	}
	
}
