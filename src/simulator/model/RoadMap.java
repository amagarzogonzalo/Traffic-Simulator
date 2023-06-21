package simulator.model;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.AddRoadContaminationException;
import exceptions.AddRoadException;
import exceptions.AddVehicleException;
import exceptions.AddVehicletoRoadException;
import exceptions.SetSpeedException;
import exceptions.VehicleMovetoNextRoadException;

public class RoadMap {
	private List<Junction> list_junctions;
	private List<Road> list_roads;
	private List <Vehicle> list_vehicles;
	private Map <String,Junction> junctions_map;
	private Map <String,Road> roads_map;
	private Map <String,Vehicle> vehicles_map;

	public RoadMap(List<Junction> list_j, List<Road> list_r, List <Vehicle> list_v,
			Map <String,Junction> junctions_m, Map <String,Road> roads_m, Map <String,Vehicle> vehicles_m) {
		list_junctions = list_j;
		list_roads = list_r;
		list_vehicles = list_v;
		junctions_map = junctions_m;
		roads_map = roads_m;
		vehicles_map = vehicles_m;
	}
	
	public void addJunction(Junction j) {
		boolean exists_junction = false;
		for(int i = 0; i < list_junctions.size() && !exists_junction; i++)
			if(list_junctions.get(i).getId().equals(j.getId())) exists_junction = true;
		if(!exists_junction) {
			list_junctions.add(j);
			junctions_map.put(j.getId(), j);
		}		
	}
	
	public void addRoad(Road r) throws AddRoadException {
		boolean exists_road = false;
		int junctions_exists = 0;
		for(int i = 0; i < list_roads.size() && !exists_road; i++)
			if(list_roads.get(i).getId().equals(r.getId())) exists_road = true;
		for(Junction j: list_junctions) {
			if(j.equals(r.getDestination())) junctions_exists++;
			else if(j.equals(r.getSource())) junctions_exists++;
		}
		if(!exists_road && junctions_exists == 2) { 
			list_roads.add(r);
			roads_map.put(r.getId(),r);
			}
		else throw new AddRoadException();
	}
	
	public void addVehicle (Vehicle v) throws AddVehicleException {
		boolean exists_vehicle = false, exists_itinerary = true;
		for(int i = 0; i < list_vehicles.size() && !exists_vehicle; i++)
			if(list_vehicles.get(i).getId().equals(v.getId())) exists_vehicle = true;
		
		for(int i = 0; i < (v.getItinerary().size()-1) && exists_itinerary; i++) {
			boolean eSource = false, eDest = false;
			if(i == 0) eDest = true;
			for(Road r: this.list_roads) {
				if(v.getItinerary().get(i).equals(r.getSource())) eSource = true;
				if(v.getItinerary().get(i).equals(r.getDestination())) eDest = true;
			}
			if(!eSource || !eDest) exists_itinerary = false;
		}	
		
		if(!exists_vehicle && exists_itinerary) { 
			list_vehicles.add(v);
			vehicles_map.put(v.getId(),v);
		
		}
		else throw new AddVehicleException();
	}

	public Junction getJunction(String id) {
		Junction result = null;
		for(int i = 0; i < list_junctions.size(); i++) 
			if(id.equals(list_junctions.get(i).getId()))
				result = list_junctions.get(i);
		return result;
	}
	
	public Road getRoad(String id) {
		Road result = null;
		for(int i = 0; i < list_roads.size(); i++) 
			if(id.equals(list_roads.get(i).getId()))
				result = list_roads.get(i);
		return result;
	}
	
	public Vehicle getVehicle(String id) {
		Vehicle result = null;
		for(int i = 0; i < list_vehicles.size(); i++) 
			if(id.equals(list_vehicles.get(i).getId()))
				result = list_vehicles.get(i);
		return result;
	}
	
	public List<Junction> getJunctions(){
		return list_junctions;
	}
	
	public List<Road> getRoads(){
		return list_roads;
	}
	
	public List<Vehicle> getVehicles(){
		return list_vehicles;
	}
	
	public void reset() {
		list_junctions.clear();
		list_roads.clear();
		list_vehicles.clear();
		junctions_map.clear();
		roads_map.clear();
		vehicles_map.clear();
	}
	
	public void advance(int time) throws VehicleMovetoNextRoadException, AddVehicletoRoadException, SetSpeedException, AddRoadContaminationException {
		for(Vehicle v: list_vehicles) v.enterFirstRoad();
		for(Junction j: list_junctions) j.advance(time);
		for(Road r: list_roads) r.advance(time);
	}
	
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		JSONArray junc = new JSONArray();
		for(Junction j: this.list_junctions) junc.put(j.report());
		obj.put("junctions", junc);
		JSONArray rod = new JSONArray();
		for(Road r: this.list_roads) rod.put(r.report());
		obj.put("roads", rod);
		JSONArray veh = new JSONArray();
		for(Vehicle v: this.list_vehicles) veh.put(v.report());
		obj.put("vehicles", veh);
		return obj;
	}
	
	public List<Junction> getList_junctions() {
		return list_junctions;
	}

	public List<Road> getList_roads() {
		return list_roads;
	}

	public List<Vehicle> getList_vehicles() {
		return list_vehicles;
	}
	
}
