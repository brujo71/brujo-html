package it.brujo.html;

public class AttrID extends Attr {

	public final static String nameId="id";
	
	AttrID() {
		super(nameId);
	}

	@Override
	public Attr merge(String value) {
		return setValue(value);
	}

	
	
}
