package exceptions;

@SuppressWarnings("serial")
public class SetContaminationException extends Exception {
	public SetContaminationException() {
		super("La contaminación debe estar entre 0 y 10.");
		
	}
	
}
