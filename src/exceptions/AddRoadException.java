package exceptions;

@SuppressWarnings("serial")
public class AddRoadException extends Exception {
	public  AddRoadException() {
		super("El identificador de la carretera debe ser nuevo y los cruces (fuente y destino) deben existir en el mapa");
	}
}
