package exceptions;

@SuppressWarnings("serial")
public class VehicleMovetoNextRoadException extends Exception {
	public VehicleMovetoNextRoadException() {
		super("Para que el vehículo se mueva a la siguiente carretera el estado del vehículo debe ser Pending o Waiting");
	}
}
