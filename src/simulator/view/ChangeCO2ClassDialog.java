package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import exceptions.NewSetContClassEventException;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.NewSetContClassEvent;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class ChangeCO2ClassDialog extends JDialog {
	private JPanel cod;
	private JComboBox<String> vehicles;
	private JComboBox<Integer> contclass;
	private Controller ctrl;
	private JSpinner ticks;
	private String selected_vehicle;
	private int selected_contclass;
	
	
	public ChangeCO2ClassDialog(Controller _ctrl)  {
		ctrl = _ctrl;
		selected_contclass = 0;
		initGUI();
	}
	
	private void initGUI() {
		cod = new JPanel();
		cod.setLayout(new BoxLayout(cod, BoxLayout.Y_AXIS));
		addDescription();
		addOptions();
		addButtons();
		setContentPane(cod);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
	}
	
	private void addDescription() {
		JPanel description = new JPanel();
		description.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel desc = new JLabel ("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		description.add(desc);
		cod.add(description);
	}
	
	private void addOptions() {
		JPanel options = new JPanel();
		options.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		List<Vehicle> list_v = ctrl.getList_vehicles();
		String[] v_ids = new String[list_v.size()];
		for(int i = 0; i < list_v.size(); i++) 
			v_ids[i] = list_v.get(i).getId();
			
		vehicles = new JComboBox<String>(v_ids); 
		JLabel desc_v = new JLabel ("Vehicle: ");
		options.add(desc_v);
		options.add(vehicles);
		options.add(Box.createRigidArea(new Dimension(25,0)));

		vehicles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == vehicles) selected_vehicle = (String)vehicles.getSelectedItem();
			}
			
		});
		
		Integer []c_nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		contclass = new JComboBox<Integer> (c_nums);
		JLabel desc_c = new JLabel("CO2 Class: ");
		options.add(desc_c);
		options.add(contclass);
		options.add(Box.createRigidArea(new Dimension(25,0)));

		contclass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(arg0.getSource() == contclass) selected_contclass = (Integer)contclass.getSelectedItem();	
			}
		});
		
		ticks = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
		JLabel desc_t = new JLabel("Ticks: ");
		options.add(desc_t);
		options.add(ticks);
		cod.add(options);
	}
	
	private void addButtons() {
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton cancel = new JButton("Cancel");
		buttons.add(cancel);
		buttons.add(Box.createRigidArea(new Dimension(32,0)));
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(arg0.getSource() == cancel) {
					setVisible(false);
				}
			}
		});
		JButton ok = new JButton("OK");
		buttons.add(ok);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if(arg0.getSource() == ok) {
					try {
						List<Pair<String,Integer>> cs = new ArrayList<Pair<String, Integer>> ();
						Pair<String,Integer> p = new Pair<String,Integer>(selected_vehicle, selected_contclass);
						cs.add(p);
						int t = (int)ticks.getValue() + ctrl.getTime();
						NewSetContClassEvent e = new NewSetContClassEvent(t, cs);
						ctrl.addEvent(e);
					}catch(NewSetContClassEventException ex) {
						ex.getMessage();
					}
					setVisible(false);
				}
			}
		});
		cod.add(buttons);
	}
	
}
