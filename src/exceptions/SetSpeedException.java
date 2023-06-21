package exceptions;


@SuppressWarnings("serial")
public class SetSpeedException extends Exception {
	public SetSpeedException() {
		super("La velocidad es menor que 0.");
	}
}
