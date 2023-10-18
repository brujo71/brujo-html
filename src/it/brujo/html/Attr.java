package it.brujo.html;


public class Attr {

	public final static String id="id";
	public final static String clazz="class";
	public final static String src="src";
	public final static String style="style";
	public final static String href="href";
	
	
	private String name;
	private String value=null;
	Elem elem=null;
	
	protected Attr(String name) {
		this.name = name;
	}
	
	static Attr create(Attr other) {
		Attr res=create(other.getName());
		res.setValue(other.getValue());
		return res;
	}
	
	public static Attr createId(String value) {
		return new AttrID().setValue(value);
	}
	
	public static Attr createClass(String value) {
		return new AttrClass().setValue(value);
	}
	
	public static Attr createStyle(String value) {
		return new AttrStyle().setValue(value);
	}
	
	public static Attr createHref(String value) {
		return new AttrHref().setValueHtml(value);
	}

	public static Attr create(String name,String value) {
		return Attr.create(name).setValue(value);
	}
	
	public static Attr create(String name) {
		Attr attr;
		if (AttrID.nameId.equalsIgnoreCase(name)) {
			attr= new AttrID();
		}
		else if (AttrClass.nameClass.equalsIgnoreCase(name)) {
			attr= new AttrClass();
		}
		else if (AttrStyle.nameStyle.equalsIgnoreCase(name)) {
			attr= new AttrStyle();
		}
		else if (AttrHref.nameHref.equalsIgnoreCase(name)) {
			attr= new AttrHref();
		}
		else {
			attr= new Attr(name);
		}
		return attr;
	}
	
	static Attr createXML(String name) {
		return new Attr(name);
	}

	public String getName() {
		return name;
	}

	public String getNameLC() {
		return name.toLowerCase();
	}

	public String getValue() {
		return value;
	}

	public Attr setValue(String value) {
		this.value = value;
		return this;
	}
	
	//override to implement different behaviour
	public Attr merge(String value) {
		if (value!=null)
			this.value+=value; 
		return this;
	}

	@Override
	public String toString() {
		return "Attr [name=" + name + ", value=" + value + "]";
	}
	
	void write(Writer out) {
		out.write(getName());
		if (value!=null) {
			out.write("=\"");
			out.write(getValue());
			out.write("\"");
		}
		
	}
	
	public Elem elem() {
		return elem;
	}
	
	public Attr attrClone() {
		return create(this);
	}
}
