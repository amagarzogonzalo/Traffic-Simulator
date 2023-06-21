package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Junction> junctions;
	String cols[] = {"Id", "Green", "Queues"};
	
	public JunctionsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		junctions = new ArrayList<Junction>();
		ctrl.addObserver(this);
	}
	
	private void update(List<Junction> njunctions) {
		junctions = njunctions;
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map.getJunctions());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map.getJunctions());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map.getJunctions());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map.getJunctions());
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override 
	public String getColumnName(int column) {
		return cols[column];
	}
	
	@Override
	public int getColumnCount() {
		return cols.length;
	}


	@Override
	public int getRowCount() {
		return junctions.size();
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Junction j = junctions.get(rowIndex);
		String result = "";
		switch(columnIndex) {
		case 0:
			result = "" + j.getId();
			break;
		case 1: 
			if(j.getGreen_light_index() == -1) result = "NONE";
			else result = j.getIncoming_roads().get(j.getGreen_light_index()).getId();
			break;
		case 2: 
			for(Road r: j.getIncoming_roads()) 
				result = result + " " + r.getId() + ": " + j.getQueue(r);
			break;
		}
		
		return result;
	}

}
