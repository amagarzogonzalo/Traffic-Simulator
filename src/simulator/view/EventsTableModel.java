package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

 public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	String[] cols = {"Time", "Desc."};
	private List<Event> list;
	
	public EventsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		list = new ArrayList<Event>();
	}
	
	private void update(List<Event> events_new, int time) {
		list.removeAll(list);
		for(int i = 0;i < events_new.size(); i++) {
			if(events_new.get(i).getTime() > time) list.add(events_new.get(i));
		}
		fireTableDataChanged();
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {	
		update(events, time);

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {	
		update(events, time);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		list.removeAll(list);
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {		
		update(events, time);
	}

	@Override
	public void onError(String err) {
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
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Event e = list.get(rowIndex);
		String result = "";
		switch(columnIndex) {
		case 0:
			result = "" + e.getTime();
			break;
		case 1: 
			result = result + e.toString();
			break;
		}
		
		return result;
	}
}
