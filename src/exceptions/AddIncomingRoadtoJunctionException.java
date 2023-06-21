package exceptions;

@SuppressWarnings("serial")
public class AddIncomingRoadtoJunctionException extends Exception {
	public AddIncomingRoadtoJunctionException() {
		super("El destino de la carretera añadida debe ser ese cruce.");
	}
}
