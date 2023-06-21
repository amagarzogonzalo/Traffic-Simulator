package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import exceptions.LoadEventsControllerException;
import exceptions.NewSetContClassEventException;
import exceptions.SetWeatherEventException;
import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl ) throws FileNotFoundException, SetWeatherEventException, LoadEventsControllerException, NewSetContClassEventException {
		super ( "Traffic Simulator" );
		_ctrl = ctrl ;
		initGUI();
	}
	
	private void initGUI() throws FileNotFoundException, SetWeatherEventException, LoadEventsControllerException, NewSetContClassEventException {
		JPanel mainPanel = new JPanel( new BorderLayout()); 
		this .setContentPane( mainPanel ); 
		mainPanel .add( new ControlPanel( _ctrl ), BorderLayout. PAGE_START);
		mainPanel .add( new StatusBar( _ctrl ),BorderLayout. PAGE_END ); 
	
		
		JPanel viewsPanel = new JPanel( new GridLayout(1, 2));
		mainPanel .add( viewsPanel , BorderLayout. CENTER );
		JPanel tablesPanel = new JPanel();
		tablesPanel .setLayout( new BoxLayout( tablesPanel , BoxLayout. Y_AXIS ));
		viewsPanel .add( tablesPanel );
		JPanel mapsPanel = new JPanel(); 
		mapsPanel .setLayout( new BoxLayout( mapsPanel , BoxLayout. Y_AXIS ));
		viewsPanel .add( mapsPanel );
		
		// tables
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel (_ctrl)), "Junctions");

		Border b = null;

		eventsView .setPreferredSize( new Dimension(500, 200));
		eventsView.setBorder(BorderFactory.createTitledBorder(b, "Events", TitledBorder.LEFT, TitledBorder.TOP));
		vehiclesView .setPreferredSize( new Dimension(500, 200));
		vehiclesView.setBorder(BorderFactory.createTitledBorder(b, "Vehicles", TitledBorder.LEFT, TitledBorder.TOP));
		roadsView .setPreferredSize( new Dimension(500, 200));
		roadsView.setBorder(BorderFactory.createTitledBorder(b, "Roads", TitledBorder.LEFT, TitledBorder.TOP));
		junctionsView .setPreferredSize( new Dimension(500, 200));
		junctionsView.setBorder(BorderFactory.createTitledBorder(b, "Junctions", TitledBorder.LEFT, TitledBorder.TOP));

		tablesPanel .add( eventsView );
		tablesPanel .add( vehiclesView );
		tablesPanel .add( roadsView );
		tablesPanel .add( junctionsView );
		
		// maps
		JPanel mapView = createViewPanel( new MapComponent( _ctrl ), "Map" );
		mapView .setPreferredSize( new Dimension(500, 400));
		mapView.setBorder(BorderFactory.createTitledBorder(b, "Map", TitledBorder.LEFT, TitledBorder.TOP));
		mapsPanel .add( mapView );
		JPanel mapbyroad = createViewPanel (new MapByRoadComponent(_ctrl), "MapByRoad");
		mapbyroad.setPreferredSize(new Dimension(300,200));
		mapbyroad.setBorder(BorderFactory.createTitledBorder(b, "Map by Road", TitledBorder.LEFT, TitledBorder.TOP));
		mapsPanel.add(mapbyroad);
		
		this .setDefaultCloseOperation( EXIT_ON_CLOSE );
		this .pack();
		this .setVisible( true );
		
	}
	
	private JPanel createViewPanel(JComponent c , String title ) {
		JPanel p = new JPanel( new BorderLayout() );
		// TODO add a framed border to p with title
		p .add( new JScrollPane( c ));
		return p ;
	}
}