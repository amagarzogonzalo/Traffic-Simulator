package exceptions;

@SuppressWarnings("serial")
public class AddVehicleException extends Exception {
	public AddVehicleException() {
		super("No se pudo añadir correctamente el vehículo a roadmap.");
	}
}
