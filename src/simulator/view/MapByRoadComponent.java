package simulator.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private RoadMap _map;

	
	public MapByRoadComponent(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(Color.WHITE);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getRoads().isEmpty()) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		List<Road> l = _map.getList_roads();
		for(int i = 0; i < l.size(); i++) {
			int x1 = 50, x2 = getWidth() - 100, y = (1+i)*50;
			g.setColor(Color.GRAY);
			g.drawLine(x1, y, x2, y);
			g.setColor(Color.BLUE);
			g.fillOval(x1 - 5, y - 5, 10, 10);
			g.setColor(Color.ORANGE);
			g.drawString(l.get(i).getSource().getId(), x1, y - 15);	
			g.drawString(l.get(i).getDestination().getId(), x2, y - 15);
			g.setColor(Color.RED);
			int idx = l.get(i).getDestination().getGreen_light_index();
			if (idx != -1 && l.get(i).equals(l.get(i).getDestination().getIncoming_roads().get(idx))) g.setColor(Color.GREEN);
			g.fillOval(x2 - 5, y - 5, 10, 10);
			g.setColor(Color.BLACK);
			g.drawString(l.get(i).getId(), x1- 30, y + 5);	
			List<Vehicle> lv = l.get(i).getVehicles();
			for(int j =0; j < lv.size(); j++) {
				int x = x1 + (int) ((x2 - x1) * ((double) lv.get(j).getLocation() / (double) l.get(i).getLength()));
				g.drawImage(loadImage("car.png"), x, y - 10, 16, 16, this);
				g.setColor(Color.GREEN);
				g.drawString(lv.get(j).getId(), x, y - 10);
			}
			String s = "";
			Weather w = l.get(i).getWeather_conditions();
			if(w.equals(Weather.CLOUDY)) s = "cloud";
			else if(w.equals(Weather.SUNNY)) s= "sun";
			else if(w.equals(Weather.WINDY)) s= "wind";
			else if(w.equals(Weather.STORM)) s= "storm";
			else if(w.equals(Weather.RAINY)) s = "rain";
			g.drawImage(loadImage(s +".png"), x2 + 14, y - 18, 32, 32, this);
			int c= (int) Math.floor(Math.min((double) l.get(i).getTotal_contamination()/(1.0 + (double) l.get(i).getContamination_alarm_limit()),1.0) / 0.19);
			g.drawImage(loadImage("cont_" + c + ".png"), x2 + 50, y - 18, 32, 32, this);
		}
	}
	
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {		
	}

}
