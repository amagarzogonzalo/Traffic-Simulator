package exceptions;

@SuppressWarnings("serial")
public class LoadEventsControllerException extends Exception {
	public LoadEventsControllerException () {
		super("La entrada JSON no encaja.");
	}
}
