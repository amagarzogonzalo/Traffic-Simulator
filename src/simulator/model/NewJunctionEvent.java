package simulator.model;

import exceptions.JunctionParametersException;

public class NewJunctionEvent extends Event {
	private Junction junction;
	private String id;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	int xCoor, yCoor;
	
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	@Override
	void execute(RoadMap map) throws JunctionParametersException {
		junction = new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor);
		map.addJunction(junction);
	}

	@Override
	public String toString() {
		return "New Junction '" + id + "'";
	}

}
