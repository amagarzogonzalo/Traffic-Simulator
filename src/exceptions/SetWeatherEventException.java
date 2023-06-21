package exceptions;

@SuppressWarnings("serial")
public class SetWeatherEventException extends Exception {
	public SetWeatherEventException () {
		super("El parámetro ws no puede ser null.");
	}
}
