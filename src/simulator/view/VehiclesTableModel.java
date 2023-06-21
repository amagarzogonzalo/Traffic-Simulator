package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver  {
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	String[] cols = {"Id","Location", "Itinerary","CO2 Class", "Max speed", "Speed", "Total CO2", "Distance"};
	private List<Vehicle> list;
	
	public VehiclesTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
	}
	
	private void update(List<Vehicle> v) {
		list = v;
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String result = "";
		Vehicle v = list.get(rowIndex);
		switch(columnIndex) {
		case 0:
			result = v.getId();
			break;
		case 1: 
			result = v.getRoad().getId() + ": " + v.getLocation();
			break;
		case 2:
			result = "[";
			for(Junction j: v.getItinerary()) {
				if(j != v.getItinerary().get(v.getItinerary().size()-1)) result = result + j.getId()+ ", ";
				else result = result + j.getId();
			}
			result = result + "]";
			break;
		case 3:
			result = result + v.getContamination_class();
			break;
		case 4:
			result = result+ v.getMaximum_speed();
			break;
		case 5:
			result = result + v.getCurrent_speed();
			break;
		case 6:
			result = result + v.getTotal_contamination();
			break;
		case 7:
			result = result + v.getTotal_travelled_distance();
			break;
		}
		return result;
	}
	

	@Override 
	public String getColumnName(int column) {
		return cols[column];
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map.getList_vehicles());
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map.getList_vehicles());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map.getList_vehicles());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map.getList_vehicles());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map.getList_vehicles());
	}

	@Override
	public void onError(String err) {
	}

}
