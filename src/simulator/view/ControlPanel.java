package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.*;

import org.json.JSONException;

import exceptions.LoadEventsControllerException;
import exceptions.NewSetContClassEventException;
import exceptions.SetWeatherEventException;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements TrafficSimObserver {
	private JToolBar tb;
	private JSpinner ticks;
	private Controller ctrl;
	private boolean _stopped;
	public ControlPanel(Controller _ctrl) throws FileNotFoundException, SetWeatherEventException, LoadEventsControllerException, NewSetContClassEventException {
		ctrl = _ctrl;
		_stopped = false;
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() throws FileNotFoundException, SetWeatherEventException, LoadEventsControllerException, NewSetContClassEventException {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(createToolBar());
		setVisible(true);
	}
	
	private void load_file() throws FileNotFoundException, JSONException, LoadEventsControllerException, SetWeatherEventException, NewSetContClassEventException {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(fc);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fc.getSelectedFile();
				if(file.exists()) {
					ctrl.reset();
					InputStream in = new FileInputStream(file);
					ctrl.loadEvents(in);
				}
				else JOptionPane.showMessageDialog(this, "File does not exist.", "Inane warning", JOptionPane.ERROR_MESSAGE);

			} catch (Exception e) {
				 JOptionPane.showMessageDialog(this, "File does not exist.", "Inane warning", JOptionPane.ERROR_MESSAGE);
			}
		} 	
	}
	
	private void addSpace (int space_x) {
		tb.add(Box.createRigidArea(new Dimension(space_x,0)));
		
	}
	
	private void addSeparator() {
		JSeparator s = new JSeparator(SwingConstants.VERTICAL);
		tb.add(s);
	}
	
	private JToolBar createToolBar() {
		tb = new JToolBar();
		initButtons();
		
		JLabel l = new JLabel("Ticks: ");
		tb.add(l);
		addSpace(3);
		ticks = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
		ticks.setToolTipText("Ticks");
		tb.add(ticks);
		addSpace(935);
		addSeparator();
		addSpace(5);
		JButton exit = new JButton();
		exit.setIcon(new ImageIcon("resources/icons/exit.png"));
		exit.setToolTipText("Exit");
		tb.add(exit);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == exit) {
					Component frame = null;
					if(JOptionPane.showOptionDialog(frame, "Would you like exit?", "Confirmation to exit",JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null,	null, null) == 0) System.exit(0);
				}
			}
		});

		return tb;
	}
	
	private void initButtons() {
		JButton selectFile = new JButton();
		selectFile.setIcon(new ImageIcon("resources/icons/open.png"));
		selectFile.setToolTipText("LoadFiles");
		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == selectFile) {
					try {
						load_file();
					} catch (FileNotFoundException | JSONException | LoadEventsControllerException
							| SetWeatherEventException | NewSetContClassEventException e1) {
					}
				}
			}
		});
		tb.add(selectFile);
		addSpace(3);
		addSeparator();
		addSpace(2);
		JButton changeCo2 = new JButton();
		changeCo2.setIcon(new ImageIcon("resources/icons/co2class.png"));
		changeCo2.setToolTipText("Change CO2 Class");
		tb.add(changeCo2);
		changeCo2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == changeCo2) {
					@SuppressWarnings("unused")
					ChangeCO2ClassDialog co2dialog = new ChangeCO2ClassDialog(ctrl);		
				}
			}
		});
		JButton climateC = new JButton();
		climateC.setIcon(new ImageIcon("resources/icons/weather.png"));
		climateC.setToolTipText("Change Weather");
		climateC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == climateC) {
					@SuppressWarnings("unused")
					ChangeWeatherDialog wdialog = new ChangeWeatherDialog(ctrl);
				}
			}
		});
		tb.add(climateC);
		addSpace(3);
		JSeparator s2 = new JSeparator(SwingConstants.VERTICAL);
		tb.add(s2);
		addSpace(2);
		JButton run = new JButton();
		run.setIcon(new ImageIcon("resources/icons/run.png"));
		run.setToolTipText("Run");
		run.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == run) {
					_stopped = false;
					run_sim((int) ticks.getValue());
				}
			}
			
		});
		tb.add(run);
		JButton stop = new JButton();
		stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		stop.setToolTipText("Stop");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == stop) {
					stop();
				}
			}
		});
		tb.add(stop);
		addSpace(4);
		
	}
	
	@SuppressWarnings("deprecation")
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				ctrl.run(1);
			} catch (Exception e) {
				System.out.println("Error ejecución.");
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			tb.enable(true);
			_stopped = true;
		}
	}
	
	private void stop() {
		_stopped = true;
	}

	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		ticks.setValue(1);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onError(String err) {
		
	}

}
