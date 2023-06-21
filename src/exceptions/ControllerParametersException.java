package exceptions;

@SuppressWarnings("serial")
public class ControllerParametersException extends Exception {
	public ControllerParametersException() {
		super("Los parámetros del constructor de controler no pueden ser nulos.");
	}
}
