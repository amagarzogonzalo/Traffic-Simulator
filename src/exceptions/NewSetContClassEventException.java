package exceptions;

@SuppressWarnings("serial")
public class NewSetContClassEventException extends Exception {
	public NewSetContClassEventException() {
		super("El parámetro cs no puede ser null.");
	}
}
