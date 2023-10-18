package it.brujo.html;

import it.brujo.html.Elem.Closure;

public class XMLCreate {

	public static Fragment xmlDoc() {
		return Fragment.create().add(new Special('?', "xml version=\"1.0\" encoding=\"UTF-8\""));
	}
	
	public static Elem elem(String name) {
		return Elem.create(name).setClosure(Closure.EXPLICIT);
	}
	
	public static Elem elemCompact(String name) {
		return Elem.create(name).setClosure(Closure.COMPACT);
	}
	
	public static Attr attr(String name, String value) {
		return Attr.createXML(name).setValue(value);
	}
	
}
