package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	String[] cols = {"Id", "Length","Weather", "Max speed", "Speedlimit", "Total CO2", "CO2 limit"};
	private List<Road> list;
	
	public RoadsTableModel (Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
	}
	
	private void update(List<Road> l) {
		list = l;
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
		Road r = list.get(rowIndex);
		switch(columnIndex) {
		case 0:
			result = r.getId();
			break;
		case 1: 
			result = "" + r.getLength();
			break;
		case 2:
			if(r.getWeather_conditions().equals(Weather.CLOUDY)) result = "CLOUDY";
			else if(r.getWeather_conditions().equals(Weather.RAINY)) result = "RAINY";
			else if(r.getWeather_conditions().equals(Weather.STORM))result = "STORM";
			else if(r.getWeather_conditions().equals(Weather.SUNNY))result = "SUNNY";
			else if(r.getWeather_conditions().equals(Weather.WINDY))result = "WINDY";

			break;
		case 3:
			result = result + r.getMaximum_speed();
			break;
		case 4:
			result = result+ r.getCurrent_speed_limit();
			break;
		case 5:
			result = result + r.getTotal_contamination();
			break;
		case 6:
			result = result + r.getContamination_alarm_limit();
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
		update(map.getList_roads());
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map.getList_roads());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map.getList_roads());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map.getList_roads());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map.getList_roads());
	}

	@Override
	public void onError(String err) {
	}


}
