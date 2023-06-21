package simulator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import exceptions.SetWeatherEventException;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

@SuppressWarnings("serial")
public class ChangeWeatherDialog extends JDialog {
	private JPanel wd;
	private JComboBox<String> roads, type_weather;
	private Controller ctrl;
	private String selected_road;
	private JSpinner ticks;
	private Weather weather;
	
	public ChangeWeatherDialog(Controller _ctrl) {
		ctrl = _ctrl;
		weather = Weather.CLOUDY;
		initGUI();
	}

	private void initGUI() {
		wd = new JPanel();
		wd.setLayout(new BoxLayout(wd, BoxLayout.Y_AXIS));
		addDescription();
		addOptions();
		addButtons();
		setContentPane(wd);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
	}
	
	private void addDescription() {
		JPanel description = new JPanel();
		description.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel desc = new JLabel("Schedule an event to change the weather of a road after a fiven number of simulations tick from now.");
		description.add(desc);
		wd.add(description);
	}
	
	private void addOptions() {
		JPanel options = new JPanel();
		options.setLayout(new FlowLayout(FlowLayout.CENTER));
		List<Road> list_r = ctrl.getList_roads();
		String[] r_ids = new String[list_r.size()];
		for(int i = 0; i < list_r.size(); i++) 
			r_ids[i] = list_r.get(i).getId();
		
		roads = new JComboBox<String>(r_ids);
		JLabel desc_r = new JLabel("Road: ");
		options.add(desc_r);
		options.add(roads);
		options.add(Box.createRigidArea(new Dimension(25,0)));
		roads.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == roads) selected_road = (String)roads.getSelectedItem();
			}
			
		});
		
		String[] tw = {"CLOUDY", "SUNNY", "RAINY", "STORM", "WINDY"};
		type_weather = new JComboBox<String>(tw);
		JLabel desc_w = new JLabel("Weather: ");
		options.add(desc_w);
		options.add(type_weather);
		options.add(Box.createRigidArea(new Dimension(25,0)));
		type_weather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == type_weather) {
					String weather_choosed = (String) type_weather.getSelectedItem();
					switch(weather_choosed) {
						case "CLOUDY": weather = Weather.CLOUDY; break;
						case "SUNNY": weather = Weather.SUNNY; break;
						case "RAINY": weather = Weather.RAINY; break;
						case "STORM": weather = Weather.STORM; break;
						case "WINDY": weather = Weather.WINDY; break;	
					}
				}
			}
		});
		
		ticks = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
		JLabel desc_t = new JLabel("Tick: ");
		options.add(desc_t);
		options.add(ticks);
		wd.add(options);
	}
	
	private void addButtons() {
		JPanel buttons2 = new JPanel();
		buttons2.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton cancel = new JButton("Cancel");
		buttons2.add(cancel);
		buttons2.add(Box.createRigidArea(new Dimension(32,0)));
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(arg0.getSource() == cancel) {
					setVisible(false);
				}
			}
		});
		JButton ok = new JButton("OK");
		buttons2.add(ok);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(arg0.getSource() == ok) {
					try {
						List<Pair<String,Weather>> cs = new ArrayList<Pair<String, Weather>> ();
						Pair<String,Weather> p = new Pair<String,Weather>(selected_road, weather);
						cs.add(p);
						int t = (int) ticks.getValue() +ctrl.getTime();
						SetWeatherEvent e = new SetWeatherEvent(t, cs);
						ctrl.addEvent(e);
					} catch (SetWeatherEventException e1) {
						e1.getMessage();
					}
					setVisible(false);
				}
			}
		});
		wd.add(buttons2);
	}
	
}
