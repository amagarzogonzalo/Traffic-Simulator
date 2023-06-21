package exceptions;

@SuppressWarnings("serial")
public class AddOutgoingRoadtoJunctionException extends Exception {
	public AddOutgoingRoadtoJunctionException() {
		super("La salida, la fuente, de la carretera añadida debe ser ese cruce.");
	}
}
