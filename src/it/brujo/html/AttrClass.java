package it.brujo.html;

public class AttrClass extends Attr {

	public final static String nameClass="class";
	
	AttrClass() {
		super(nameClass);
	}

	@Override
	public Attr merge(String value) {
		setValue(getValue()+" "+value);
		return this;
	}


	
}
