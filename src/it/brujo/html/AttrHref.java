package it.brujo.html;


public class AttrHref extends Attr {

	public final static String nameHref="href";
	
	AttrHref() {
		super(nameHref);
	}

	public Attr setValueHtml(String value) {
		return setValue(value==null ? null : Html.HTMLEscapeHref(value));
	}
}
