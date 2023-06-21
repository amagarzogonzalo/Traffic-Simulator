package exceptions;

@SuppressWarnings("serial")
public class SetWeatherRoadException extends Exception {
	public SetWeatherRoadException() {
		super("Weather no puede tener un valor null.");
	}
}
