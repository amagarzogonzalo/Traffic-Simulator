package simulator.model;

import java.util.ArrayList;
import java.util.List;

import exceptions.AddVehicleException;
import exceptions.VehicleParametersException;

public class NewVehicleEvent extends Event {
	private Vehicle vehicle;
	private String id;
	private int maxSpeed, contClass;
	private List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
			super(time);
			this.id = id;
			this.maxSpeed = maxSpeed;
			this.contClass = contClass;
			this.itinerary = itinerary;
	}
	
	@Override
	void execute(RoadMap map) throws VehicleParametersException, AddVehicleException {
		List <Junction> iti = new ArrayList<Junction>();
		for(int i = 0; i < itinerary.size(); i++) {
			Junction tmp = map.getJunction(itinerary.get(i));
			iti.add(tmp);
		}
		
		vehicle = new Vehicle(id, maxSpeed, contClass, iti);
		map.addVehicle(vehicle);
		
	}
	
	@Override
	public String toString() {
		return "New Vehicle '" + id + "'";
	}

}
