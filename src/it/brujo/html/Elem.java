package it.brujo.html;

import java.util.LinkedList;
import java.util.List;

public class Elem extends MultiPart {

	public static enum Closure {OPEN,COMPACT,EXPLICIT}
	
	public static Elem create(String name) {
		if (name == null || name.length() < 1) {
			throw new NullPointerException();
		}
		return new Elem(name);
	}
	
	private String name,lcName;
	private Closure closure = Closure.OPEN;
	private List<Attr> attrs = new LinkedList<>();
	
	Elem(String name) {
		setName(name);
	}
	
	public Elem rename(String name) {
		setName(name);
		return this;
	}
	
	private void setName(String name) {
		this.name = name;
		this.lcName = name == null ? null : name.toLowerCase();
	}

	public String name() {
		return name;
	}

	public String nameLC() {
		return lcName;
	}

	public Closure getClosure() {
		return closure;
	}

	public Elem setClosure(Closure closure) {
		this.closure = closure;
		return this;
	}
	
	public List<Attr> attrs() {
		return attrs;
	}

	public Elem add(Attr a) {
		if (a.elem != null && a.elem != this) {
			throw new IllegalStateException("Attr can't be assigned twice elem=" + this + " attr=" + a);
		}
		a.elem = this;
		Attr old = attr(a.getName());
		if (old == null) {
			attrs.add(a);
		} else {
			old.merge(a.getValue());
		}
		return this;
	}
	
	public Elem addAttr(String name) {
		return add(Attr.create(name));
	}
	
	public Elem addAttr(String name, String value) {
		Attr attr = Attr.create(name).setValue(value);
		return add(attr);
	}
	
	public Elem removeAttr(String name) {
		Attr attrToRemove = attr(name);
		if (attrToRemove != null)
			attrs().remove(attrToRemove);
		return this;
	}
	
	public Elem add(Part p) {
		super.addContent(p);
		return this;
	}

	public Elem add(String content) {
		super.addContent(Text.creaAsIs(content));
		return this;
	}

	public Elem addText(String content) {
		super.addContent(Text.creaText(content));
		return this;
	}

	public Elem addHtml(String htmlContent) {
		super.addAllContent(Parser.parseHtml(htmlContent).childs());
		return this;
	}
	
	public Elem addClass(String htmlClass) {
		return add(Attr.createClass(htmlClass));
	}
	
	public Elem addStyle(String style) {
		Attr old = attr(AttrStyle.nameStyle);
		if (old == null) {
			add(Attr.createStyle(style));
		} else {
			old.merge(style);
		}
		return this;
	}

	public Elem addFirst(Part p) {
		super.addContentFirst(p);
		return this;
	}
	
	public Elem set(Attr a) {
		if (a.elem != null && a.elem != this) {
			throw new IllegalStateException("Attr can't be assigned twice elem=" + this + " attr=" + a);
		}
		a.elem = this;
		Attr old = attr(a.getName());
		if (old == null) {
			attrs.add(a);
		} else {
			old.setValue(a.getValue());
		}
		return this;
	}
	
	public Elem setAttr(String name, String value) {
		Attr attr = Attr.create(name).setValue(value);
		set(attr);
		return this;
	}
	
	public Attr attr(String name) {
		for (Attr a:attrs())
			if (a.getName().equalsIgnoreCase(name))
				return a;
		return null;
	}
	
	public Elem remove() {
		if (parent!=null && parent instanceof MultiPart) {
			((MultiPart)parent).remove(this);
		}
		else {
			throw new ModificationException(this,"no parent");
		}
		return this;
	}
	
	public Elem replace(Part newPart) {
		if (parent!=null && parent instanceof MultiPart) {
			((MultiPart)parent).replace(this,newPart);
		}
		else {
			throw new ModificationException(this,"no parent");
		}
		return this;
	}
	
	
	public Elem set(Part p) {
		super.setContent(p);
		return this;
	}

	public Elem set(String s) {
		super.setContent(Text.creaAsIs(s));
		return this;
	}
	
	public Elem setText(String textToEscape) {
		super.setContent(Text.creaText(textToEscape));
		return this;
	}
	
	public Elem setHtml(String htmlToParse) {
		clearContent();
		super.addAllContent(Parser.parseHtml(htmlToParse).childs());
		return this;
	}
	
	public Elem setId(String id) {
		return add(Attr.createId(id));
	}
	
	public Elem setHref(String href) {
		removeAttr(Attr.href);
		return add(Attr.createHref(href));
	}
	
	public Elem set(List<Part> p) {
		super.setContent(p);
		return this;
	}

	@Override
	public void write(Writer out) {
		writeTag(out);
		writeTagContent(out);
		writeTagClosure(out);
	}
	
	void writeTag(Writer out) {
		out.write("<");
		out.write(name);
		if (attrs!=null) {
			for (Attr a:attrs) {
				out.write(" ");
				a.write(out);
			}
		}
		if (childs==null && closure==Closure.COMPACT) {
				out.write(" />");
		}
		else {
			out.write(">");
		}
	}
	
	void writeTagClosure(Writer out) {
		if (closure!=Closure.EXPLICIT)
			return;
		out.write("</");
		out.write(name);
		out.write(">");
	}

	public void writeTagContent(Writer out) {
		super.write(out);
	}



	@Override
	public String toString() {
		return "Elem [name=" + name + ", closure=" + closure + ", fromLine="
				+ fromLine + ", toLine=" + toLine + "]";
	}

	public boolean equalsId(String id) {
		Attr attr= attr(AttrID.nameId);
		return attr!=null && attr.getValue().equalsIgnoreCase(id);
	}

	@Override
	protected Part cloneNew() {
		return new Elem(null);
	}

	@Override
	void cloneCopy(Part other) {
		super.cloneCopy(other);
		Elem oe = (Elem) other;
		this.setName(oe.name);
		this.closure = oe.closure;
		for (Attr a : oe.attrs()) {
			add(Attr.create(a));
		}
	}
	
	public Elem myClone() {
		Elem copy = (Elem) cloneNew();
		copy.cloneCopy(this);
		return copy;
	}
	
	
	public Fragment fragment() {
		Part res = this;
		while (res != null && (!(res instanceof Fragment))) {
			res = res.parent;
		}
		return (Fragment) res;
	}
	
	public boolean closeMatch(String closerName) {
		return closure==Closure.OPEN && name().equalsIgnoreCase(closerName);
	}
	
	public ElemHtml elemHtml() {
		return new ElemHtml(this);
	}
	
	@Override
	public int debugHead(Writer out, String linePrefix) {
		out.write(linePrefix);
		out.write("Elem ");
		out.write(name);
		out.write("\n");
		return 1;
	}

	@Override
	public String contentString() {
		if (PlainTextWriter.contentBlockElemsSet.contains(lcName))
			return null;
		if (PlainTextWriter.contentTrimElemsSet.contains(lcName))
			return super.contentString(true);
		return super.contentString(false);
	}
	
	/**
	 * Ritorna un {@code Fragment} con tutti e soli i {@code Part} contenuti nel {@code Elem}. ATTENZIONE che il {@code Fragment} contiene i riferimenti ai {@code Part} originali!<br>
	 * Esempio:<br>
	 * {@code <div>Hello <span>world</span></div>} -> {@code Hello <span>world</span>}
	 * @return {@code Fragment}, può essere vuoto ma mai {@code null}
	 */
	public Fragment content() {
		Fragment res = new Fragment();
		res.childs = childs;
		return res;
	}

	@Override
	public int writeLen() {
		WriterString wa=new WriterString(256);
		writeTag(wa);
		writeTagClosure(wa);
		return super.writeLen()+wa.out().length();
	}
	
}
