package simulator.model;
import java.util.*;

import org.json.JSONObject;

import exceptions.AddRoadContaminationException;
import exceptions.AddVehicletoRoadException;
import exceptions.SetContaminationException;
import exceptions.SetSpeedException;
import exceptions.VehicleMovetoNextRoadException;
import exceptions.VehicleParametersException;

public class Vehicle extends SimulatedObject {
	private List<Junction> itinerary;
	private int maximum_speed;
	private int current_speed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contamination_class;
	private int total_contamination;
	private int total_travelled_distance;
	private int ind_itinerary;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws VehicleParametersException {
			super(id);
			if (itinerary.size() < 2) throw new VehicleParametersException("La longitud de la lista itinerary debe ser al menos 2.");
			else this.itinerary = itinerary;
			if(maxSpeed < 0) throw new VehicleParametersException("Max speed debe ser mayor que 0.");
			else maximum_speed = maxSpeed;
			current_speed = 0;
			status = VehicleStatus.PENDING;
			road = null;
			location = 0;
			if(contClass > 10 || contClass < 0) throw new VehicleParametersException("La clase de contaminación debe ser un número entre 0 y 10");
			else contamination_class = contClass;
			total_contamination = 0;
			total_travelled_distance = 0;
			ind_itinerary = 0;
			Collections.unmodifiableList(new ArrayList<>(itinerary));
	}

	public void setSpeed(int s) throws SetSpeedException {
		if(s < 0) throw new SetSpeedException();
		else {
			if(s < maximum_speed) current_speed = s;
			else current_speed = maximum_speed;
		}
	}
	
	public void setContaminationClass(int c) throws SetContaminationException {
		if(c < 0 || c > 10) throw new SetContaminationException(); 
		else contamination_class = c;
	}
	
	@Override
	void advance(int time) throws AddRoadContaminationException, VehicleMovetoNextRoadException, AddVehicletoRoadException, SetSpeedException {
		if(status.equals(VehicleStatus.TRAVELING)) {
			int oldlocation = location;
			if((location + current_speed) < road.getLength()) {
				total_travelled_distance += current_speed;
				location = location + current_speed;
			}
			else {
				total_travelled_distance += (road.getLength() - oldlocation);
				location = road.getLength(); 
			}
			int c = contamination_class * (location - oldlocation);
			total_contamination += c;
			road.addContamination(c);

			
			if(location >= road.getLength()) {
				itinerary.get(ind_itinerary).enter(this);
				status = VehicleStatus.WAITING;
			}
		}	
		
	}
	
	public void moveToNextRoad() throws VehicleMovetoNextRoadException, AddVehicletoRoadException, SetSpeedException {
		if(status.equals(VehicleStatus.PENDING) || status.equals(VehicleStatus.WAITING)) {
			if(status.equals(VehicleStatus.WAITING))road.exit(this);
			if(ind_itinerary < itinerary.size()) {
				location = 0;
				current_speed = 0;
				if(ind_itinerary == (itinerary.size()-1)) {
					status = VehicleStatus.ARRIVED;
					setSpeed(0);
				}
				else {
					road = itinerary.get(ind_itinerary).roadTo(itinerary.get(ind_itinerary+1));
					ind_itinerary++;
					status = VehicleStatus.TRAVELING;
					road.enter(this);
				}
				
			}
		}
		else {
			throw new VehicleMovetoNextRoadException();
		}
	}
	
	public void enterFirstRoad() throws VehicleMovetoNextRoadException, AddVehicletoRoadException, SetSpeedException {
		if(status.equals(VehicleStatus.PENDING)) moveToNextRoad();
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("id",getId());
		obj.put("speed", current_speed);
		obj.put("distance", total_travelled_distance);
		obj.put("co2",total_contamination);
		obj.put("class",contamination_class);
		obj.put("satuts",status);
		if(road != null) obj.put("road",road.getId());
		obj.put("localitation",location);
		return obj;
	}

	public int getCurrent_speed() {
		return current_speed;
	}

	public int getContamination_class() {
		return contamination_class;
	}

	public Road getRoad() {
		return road;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public int getLocation() {
		return location;
	}

	public void setStatus(VehicleStatus status) {
		this.status = status;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public int getMaximum_speed() {
		return maximum_speed;
	}

	public int getTotal_contamination() {
		return total_contamination;
	}

	public int getTotal_travelled_distance() {
		return total_travelled_distance;
	}
	
}
