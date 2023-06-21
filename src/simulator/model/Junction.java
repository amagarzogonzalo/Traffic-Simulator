package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.AddVehicletoRoadException;
import exceptions.JunctionParametersException;
import exceptions.SetSpeedException;
import exceptions.VehicleMovetoNextRoadException;

public class Junction extends SimulatedObject {
	private List<Road> incoming_roads;
	private Map<Junction,Road> outgoing_roads;
	private List <List<Vehicle>> queue_list;
	private int green_light_index;
	private int last_step_trafficlight;
	private LightSwitchingStrategy lightswitching;
	private DequeuingStrategy dequeuing;
	private int x, y;
	
	protected Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy
			dqStrategy, int xCoor, int yCoor) throws JunctionParametersException {
			super(id);
			if(lsStrategy == null) throw new JunctionParametersException("El parámetro lsStrategy no puede ser nulo.");
			else lightswitching = lsStrategy;
			if(dqStrategy == null) throw new JunctionParametersException("El parámetro dqStrategy no puede ser nulo.");
			else dequeuing = dqStrategy;
			last_step_trafficlight = 0;
			green_light_index = -1;
			if(xCoor < 0) throw new JunctionParametersException("El parámetro xCoor no puede ser negativo.");
			else x = xCoor;
			if(yCoor < 0)throw new JunctionParametersException("El parámetro yCoor no puede ser negativo.");
			else y = yCoor;
			incoming_roads = new ArrayList<Road>();
			outgoing_roads = new HashMap<Junction, Road>();
			queue_list = new ArrayList<List<Vehicle>>();			
	}
	
	public void addIncommingRoad(Road r) throws AddIncomingRoadtoJunctionException {
		if(r.getDestination().equals(this)){
			incoming_roads.add(r);
			queue_list.add(new ArrayList<Vehicle>());
		}
		else throw new AddIncomingRoadtoJunctionException();
		
	}
	
	
	public void addOutGoingRoad(Road r) throws AddOutgoingRoadtoJunctionException {
		if(r.getSource().equals(this) && !outgoing_roads.containsKey(r.getDestination())) outgoing_roads.put(r.getDestination(), r);
		else throw new AddOutgoingRoadtoJunctionException();
	}
	
	
	public void enter(Vehicle v) throws SetSpeedException {
		int index = 0;
		boolean found = false;
		for(int i = 0; i < incoming_roads.size() && !found; i++) {
			if(incoming_roads.get(i).equals(v.getRoad())) {
				index = i;
				found = true;
			}
		}
		if(found && (v != null)) {
			queue_list.get(index).add(v);
			v.setStatus(VehicleStatus.WAITING);
			v.setSpeed(0);
		}
	}
	
	
	public Road roadTo(Junction j) {		
		if(outgoing_roads.containsKey(j)) {
			return outgoing_roads.get(j);
		}
		else return null;
	}
	
	@Override
	void advance(int time) throws VehicleMovetoNextRoadException, AddVehicletoRoadException, SetSpeedException {
		if(green_light_index < queue_list.size() && green_light_index >= 0 && queue_list.get(green_light_index) != null) {
			List<Vehicle> list = dequeuing.dequeue(queue_list.get(green_light_index));
			for(int j = 0; j < list.size(); j++) { 
				list.get(j).moveToNextRoad();
				if(queue_list.get(green_light_index).contains(list.get(j))) queue_list.get(green_light_index).remove(list.get(j));
			}
		}
			
		
		int ind = lightswitching.chooseNextGreen(incoming_roads, queue_list, green_light_index, last_step_trafficlight, time);
		if(ind != green_light_index) {
			green_light_index = ind;
			last_step_trafficlight = time;
		}		
	}

	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("id",getId());
		if(green_light_index == -1) obj.put("green", "none");
		else obj.put("green", incoming_roads.get(green_light_index)._id);
		JSONArray queues = new JSONArray();
		for(int i = 0; i < queue_list.size(); i++) {
			JSONObject queue = new JSONObject();
			JSONArray vehicle = new JSONArray(); 	
			queue.put("road", this.incoming_roads.get(i)._id);
			if(queue_list.get(i) != null)
			for(int j = 0; j < queue_list.get(i).size(); j++) 
				vehicle.put(queue_list.get(i).get(j).getId());
			queue.put("vehicles", vehicle);
			queues.put(queue);
		}
		obj.put("queues", queues);
		return obj;
	}
	
	
	
	public List<Road> getIncoming_roads() {
		return incoming_roads;
	}

	public int getGreen_light_index() {
		return green_light_index;
	}
	
	public String getQueue(Road r){
		int index = 0;
		boolean found = false;
		for(int i = 0; i < incoming_roads.size() && !found; i++) {
			if(incoming_roads.get(i).equals(r)) {
				index = i;
				found = true;
			}
		}
		String result = "[";
		for(Vehicle v: queue_list.get(index)) {
			if(result.equals("[") || index == (queue_list.size()-1)) result = result + v.getId();
			else  result = result + v.getId() + ", ";

		}
		result = result + "]";
		return result;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	
}




