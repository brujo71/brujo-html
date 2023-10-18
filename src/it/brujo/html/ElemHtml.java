package it.brujo.html;


public class ElemHtml {

	private Elem e;

	ElemHtml(Elem e) {
		super();
		this.e = e;
	}
	
	public Elem elem() {return e;}
	
	public ElemHtml addStyle(String name,String value) {
		AttrStyle style=(AttrStyle)e.attr(AttrStyle.nameStyle);
		if (style==null) {
			style=(AttrStyle)Attr.createStyle(name+": "+value+";");
			e.set(style);
		}
		else {
			style.merge(name+": "+value+";");
		}
		return this;
	}
	
	public ElemHtml setHtml(String s) {
		e.set(Text.creaText(s));
		return this;
	}

	public ElemHtml set(String s) {
		e.set(new TextAsIs(s));
		return this;
	}

	public ElemHtml addHtml(String s) {
		e.add(Text.creaText(s));
		return this;
	}

	public ElemHtml add(String s) {
		e.add(Text.creaText(s));
		return this;
	}

	public ElemHtml addClass(String className) {
		e.addAttr(AttrID.clazz, className);
		return this;
	}

	public ElemHtml setHref(String href) {
		e.setAttr("href", Html.HTMLEscapeHref(href));
		return this;
	}
	
	public ElemHtml setSrc(String src) {
		e.setAttr("src", Html.HTMLEscapeHref(src));
		return this;
	}
	
	public ElemHtml setAction(String action) {
		e.setAttr("action", Html.HTMLEscapeHref(action));
		return this;
	}

	public ElemHtml setId(String id) {
		e.setId(id);
		return this;
	}

	public void write(Writer out) {
		e.write(out);
	}
	

}
