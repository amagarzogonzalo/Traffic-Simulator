package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimObserver {
	private Controller ctrl;
	private JLabel timel, nevent;
	boolean is_event;
	public StatusBar(Controller _ctrl) {
		ctrl = _ctrl;
		initGUI();
	}
	
	private void initGUI() {
		ctrl.addObserver(this);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		time();
		add(Box.createRigidArea(new Dimension(110,0)));
		nevent = new JLabel("");
		add(nevent);
		setVisible(true);
		is_event = false;
	}
	
	private void time() {
		timel = new JLabel("Time: " + ctrl.getTime());
		add(timel);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		timel.setText("Time: "+ time);
		is_event = false;
		for(int i = 0; i< events.size() && !is_event; i++) {
			if(events.get(i).getTime() == time) {
				is_event = true;
				nevent.setText(events.get(i).toString());
			}
		}
	}	

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		timel.setText("Time: "+ time);
		if(!is_event) nevent.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		timel.setText("Time: "+ time);
	}
	

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		timel.setText("Time: "+ time);
		nevent.setText("Welcome");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		timel.setText("Time: "+ time);
		nevent.setText("Welcome");
	}

	@Override
	public void onError(String err) {
		
	}
}
