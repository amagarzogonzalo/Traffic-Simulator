package exceptions;


@SuppressWarnings("serial")
public class AddVehicletoRoadException extends Exception {
	public AddVehicletoRoadException() {
		super("Al añadir el vehículo a la carretera su velocidad y su localización deben ser 0.");
	}
}
