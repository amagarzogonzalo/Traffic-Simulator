package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.AddRoadContaminationException;
import exceptions.AddVehicletoRoadException;
import exceptions.RoadParametersException;
import exceptions.SetSpeedException;
import exceptions.SetWeatherRoadException;
import exceptions.VehicleMovetoNextRoadException;


public abstract class Road extends SimulatedObject{
	protected Junction source = null;
	protected Junction destination = null;
	private int length;
	protected int maximum_speed;
	protected int current_speed_limit;
	protected int contamination_alarm_limit;
	protected Weather weather_conditions;
	protected int total_contamination;
	private List<Vehicle> vehicles;
	
	protected Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, 
			int contLimit, int length, Weather weather) throws RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException {
			super(id);
			if(destJunc == null) throw new RoadParametersException("Junction destination debe ser distinto de null.");
			else {
				destination = destJunc;
				destination.addIncommingRoad(this);
			}
			if(srcJunc == null) throw new RoadParametersException("Junction source debe ser distinto de null.");
			else {
				source =  srcJunc;
				source.addOutGoingRoad(this);
			}
			if(length < 0) throw new RoadParametersException("Length debe ser positivo.");
			else this.length = length;
			if(maxSpeed < 0) throw new RoadParametersException("MaxSpeed debe ser positivo.");
			else{
				this.maximum_speed = maxSpeed;
				current_speed_limit = maxSpeed;
			}
			if(contLimit < 0) throw new RoadParametersException("Contaminación limite (contLimit) debe ser positivo.");
			else this.contamination_alarm_limit = contLimit;
			if(weather == null) throw new RoadParametersException("Weather tiene que ser diferente de null.");
			else weather_conditions = weather;
			total_contamination = 0;
			vehicles = new ArrayList<Vehicle>();				
	}

	
	void enter(Vehicle v) throws AddVehicletoRoadException {
		if(v.getCurrent_speed() != 0 || v.getLocation() != 0) throw new AddVehicletoRoadException();
		else vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather w) throws SetWeatherRoadException {
		if(w.equals(null)) throw new SetWeatherRoadException();
		else this.weather_conditions = w;
	}
	
	void addContamination(int c) throws AddRoadContaminationException  {
		if(c < 0)throw new AddRoadContaminationException();
		else total_contamination += c;
	}
	
	abstract void reduceTotalContamination(); 
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	@Override
	void advance(int time) throws SetSpeedException, AddRoadContaminationException, VehicleMovetoNextRoadException, AddVehicletoRoadException {
		this.reduceTotalContamination();
		this.updateSpeedLimit();
		for(Vehicle v: vehicles) {
			if(v.getStatus().equals(VehicleStatus.TRAVELING))v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		Collections.sort(vehicles, new Comparator<Vehicle>() {
		   @Override
			public int compare(Vehicle v1, Vehicle v2) {
		    	return Integer.compare(v1.getLocation(),v2.getLocation());
		    }
		});
		Collections.sort(vehicles, (v1, v2) -> Integer.compare(v1.getLocation(),v2.getLocation()));

	}

	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("id",getId());
		obj.put("speedlimit", current_speed_limit);
		obj.put("weather",  weather_conditions);
		obj.put("co2", total_contamination);
		JSONArray arr = new JSONArray();
		for(Vehicle v: this.vehicles) arr.put(v.getId());
		obj.put("vehicles", arr);
		return obj;
	}

	public int getLength() {
		return length;
	}


	public Junction getDestination() {
		return destination;
	}


	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}

	public Junction getSource() {
		return source;
	}


	public int getContamination_alarm_limit() {
		return contamination_alarm_limit;
	}

	public int getTotal_contamination() {
		return total_contamination;
	}


	public Weather getWeather_conditions() {
		return weather_conditions;
	}


	public int getMaximum_speed() {
		return maximum_speed;
	}


	public int getCurrent_speed_limit() {
		return current_speed_limit;
	}
	
	
	
	
}
