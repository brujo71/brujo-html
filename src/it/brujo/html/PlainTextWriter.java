package it.brujo.html;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class PlainTextWriter {
	
	private Writer out;
	private boolean includeMainAttrs=true; // al momento solo href dei tag a ma potrebbe essere anche i tag alt degli img
	
	public static void writeTo(Part p,Writer out,boolean includeMainAtt) {
		new PlainTextWriter(out,includeMainAtt).write(p);
	}
	
	public static String writeToString(Part p,boolean includeMainAttrs) {
		WriterString ws=new WriterString();
		new PlainTextWriter(ws,includeMainAttrs).write(p);
		return ws.out();
	}
	
	public PlainTextWriter(Writer out,boolean includeMainAtt) {
		this.out=out;
		this.includeMainAttrs=includeMainAtt;
	}

	public void write(Part p) {
		consume(p);
	}
	
	private void consumeChilds(Collection<Part> childs) {
		if (childs!=null) {
			for (Part p:childs) {
				consume(p);
			}
		}
	}

	private void write(char c) {
		out.write(String.valueOf(c));
	}
	private static Set<Character> HtmlSpaces=new HashSet<Character>();
	static {
		HtmlSpaces.add(' ');
		HtmlSpaces.add('\n');
		HtmlSpaces.add('\r');
		HtmlSpaces.add('\t');
	}
	private boolean appendSpace=false;
	private void append(char c,boolean force) {
		if (HtmlSpaces.contains(c)) {
			if (force) 
				write(c);
			else if (!appendSpace)
				out.write(" ");
			appendSpace=true;
		}
		else {
			write(c);
			appendSpace=false;
		}
		
	}
	private void append(String s,boolean force) {
		if (s==null)
			return;
		for (char c:s.toCharArray())
			append(c, force);
	}

	private void consumeElem(Elem elem) {
		if (contentBlockElemsSet.contains(elem.nameLC()))
			return ;
		if (elem.nameLC().contentEquals("br")) {
			append('\n',true);
			return;
		}
		consumeChilds(elem.childs());
		if (elem.nameLC().contentEquals("p") || elem.nameLC().contentEquals("div") || elem.nameLC().contentEquals("tr")) {
			append('\n',true);
			return;
		}
	}
	
	private void consume(Part p) {
		if (p instanceof Elem) {
			consumeElem((Elem)p);
			if (includeMainAttrs && ((Elem)p).nameLC().equals("a")) {
				append(" [",false);
				append(((Elem)p).attr("href").getValue(),false);
				append("] ",false);
			}
		}
		else if (p instanceof Text) {
			append(((Text)p).plainText(),false);
		}
		else if (p instanceof Fragment) {
			consumeChilds(((Fragment) p).childs());
		}
	}

	static Set<String> contentBlockElemsSet=new HashSet<>();
	static Set<String> contentTrimElemsSet=new HashSet<>();

	static {
		contentTrimElemsSet.add("table");
		contentTrimElemsSet.add("tbody");
		contentTrimElemsSet.add("tfoot");
		contentTrimElemsSet.add("tr");
		contentTrimElemsSet.add("ul");
		contentTrimElemsSet.add("ol");
		
	}
	

	static {
		contentBlockElemsSet.add("doctype");
		contentBlockElemsSet.add("head");
		contentBlockElemsSet.add("style");
		contentBlockElemsSet.add("script");
		contentBlockElemsSet.add("link");
	}
	

}
