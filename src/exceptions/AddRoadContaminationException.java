package exceptions;

@SuppressWarnings("serial")
public class AddRoadContaminationException extends Exception {
	public AddRoadContaminationException (){
		super("La contaminación añadida a una carretera ha de ser positiva.");
	}
}
