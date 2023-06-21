package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONException;

import exceptions.AddIncomingRoadtoJunctionException;
import exceptions.AddOutgoingRoadtoJunctionException;
import exceptions.AddRoadContaminationException;
import exceptions.AddRoadException;
import exceptions.AddVehicleException;
import exceptions.AddVehicletoRoadException;
import exceptions.ControllerParametersException;
import exceptions.JunctionParametersException;
import exceptions.LoadEventsControllerException;
import exceptions.NewSetContClassEventException;
import exceptions.RoadParametersException;
import exceptions.SetContaminationException;
import exceptions.SetSpeedException;
import exceptions.SetWeatherEventException;
import exceptions.SetWeatherRoadException;
import exceptions.VehicleMovetoNextRoadException;
import exceptions.VehicleParametersException;
import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.*;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static int time;
	private static boolean gui;

	private static void parseArgs(String[] args) {
		gui = true;
		// define the valid command line options
		Options cmdLineOptions = buildOptions();
		// parse the command line as provided in args
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTimeOption(line);
			// if there are some remaining arguments, then something wrong is provided in the command line!
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg().desc("Ticks").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Using GUI.").build());

		return cmdLineOptions;
	}

	private static void parseModeOption(CommandLine line) {
		if(line.hasOption("m")) {
			if(line.getOptionValue("m").equals("console")) gui = false;
		}
	}
	
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseTimeOption(CommandLine line) throws ParseException{
		if(line.hasOption("t")) {
			time = Integer.parseInt(line.getOptionValue("t"));
		}
		else time = _timeLimitDefaultValue;
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		if(line.hasOption("i")) {
			_inFile = line.getOptionValue("i");
			if (_inFile == null) {
				throw new ParseException("An events file is missing");
			}
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if(!gui)_outFile = line.getOptionValue("o");
	}
	
	
	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory <>(lsbs);
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory,dqsFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		Factory<Event> eventsFactory = new BuilderBasedFactory<>(ebs);
		_eventsFactory = eventsFactory;
	}

	private static void startBatchMode() throws IOException, ControllerParametersException, JSONException, LoadEventsControllerException, SetWeatherEventException, NewSetContClassEventException, RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException, AddRoadException, JunctionParametersException, SetContaminationException, VehicleParametersException, AddVehicleException, SetWeatherRoadException, VehicleMovetoNextRoadException, SetSpeedException, AddRoadContaminationException, AddVehicletoRoadException {
		TrafficSimulator tf = new TrafficSimulator();
		Controller control = new Controller(tf, _eventsFactory);
		try {
			InputStream in = new FileInputStream(_inFile);
			control.loadEvents(in);
			if(_outFile == null) {
				control.run(time, System.out);
			}
			else {
				OutputStream out = new FileOutputStream(_outFile);
				control.run(time, out);
			}
		}catch(RuntimeErrorException e) {
			System.out.println("Error de ejecución.");
		}
	}
	
	// example command lines: 
		//-i resources/examples/ex1.json
		//-i resources/examples/ex1.json -t 30	
		//-i resources/examples/ex1.json -o resources/tmp/ex1.out.json	
		// --help
	private static void startGUIMode() throws ControllerParametersException, FileNotFoundException, JSONException, LoadEventsControllerException, SetWeatherEventException, NewSetContClassEventException {
		TrafficSimulator tf = new TrafficSimulator();
		Controller ctrl = new Controller(tf, _eventsFactory);
		if(_inFile != null) {
			InputStream in = new FileInputStream(_inFile);
			ctrl.loadEvents(in);
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new MainWindow(ctrl);
				} catch (FileNotFoundException | SetWeatherEventException | LoadEventsControllerException
						| NewSetContClassEventException e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	private static void start(String[] args) throws IOException, ControllerParametersException, JSONException, LoadEventsControllerException, SetWeatherEventException, NewSetContClassEventException, RoadParametersException, AddIncomingRoadtoJunctionException, AddOutgoingRoadtoJunctionException, AddRoadException, JunctionParametersException, SetContaminationException, VehicleParametersException, AddVehicleException, SetWeatherRoadException, VehicleMovetoNextRoadException, SetSpeedException, AddRoadContaminationException, AddVehicletoRoadException {
		initFactories();
		parseArgs(args);
		if (gui) startGUIMode();
		else startBatchMode();
	}

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
