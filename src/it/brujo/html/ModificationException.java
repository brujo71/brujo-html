package it.brujo.html;

public class ModificationException extends RuntimeException {

	Part part;
	

	
	public ModificationException(Part part,String message) {
		super(message);
		this.part = part;
	}


	@Override
	public String getMessage() {
		return (part==null ? "" : part.toString())+" "+super.getMessage();
	}




	private static final long serialVersionUID = 1L;

}
