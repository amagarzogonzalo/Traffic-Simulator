package simulator.model;

import java.util.List;
import exceptions.NewSetContClassEventException;
import exceptions.SetContaminationException;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	private List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws NewSetContClassEventException {
		super(time);
		if(cs.equals(null)) throw new NewSetContClassEventException();
		else this.cs = cs;
	}
	
	@Override
	void execute(RoadMap map) throws SetContaminationException {
		for(Pair<String, Integer> c: cs) 
			map.getVehicle(c.getFirst()).setContaminationClass(c.getSecond());

	}

	@Override
	public String toString() {
		return "New ContClass " + cs.get(0).getFirst().toString() + ", " + cs.get(0).getSecond().toString();

	}
	
}
