package it.brujo.html;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Query implements Iterable<Elem> {
	
	List<Elem> list=new LinkedList<>();
	
	public Query(MultiPart target) {
		target.childs().stream().filter(p -> p instanceof Elem).forEach(p -> add((Elem)p));
	}

	public Query(Query query,Predicate<Elem> filter) {
		query.forEach(e -> e.walkElemTree(elem -> { if (filter.test(elem)) add(elem); }));
	}

	public Query all() {
		return new Query(this,e -> true);
	}
	
	public Query filter(Predicate<Elem> filter) {
		return new Query(this, filter);
	}

	public Query filterName(final String elemName) {
		return new Query(this,  e -> {return e.name().equalsIgnoreCase(elemName);});
	}

	public Query filterId(final String id) {
		return new Query(this,  e -> {Attr a=e.attr(AttrID.nameId); return a!=null && a.getValue()!=null && a.getValue().equalsIgnoreCase(id);});
	}
	
	public Query filterHasClass(final String clazz) {
		return new Query(this,  e -> filterHasClass(e, clazz));
	}
	private boolean filterHasClass(Elem e,String clazz) {
		Attr classAttr=e.attr(Attr.clazz);
		if (classAttr==null || classAttr.getValue()==null )
			return false;
		String value=classAttr.getValue().toLowerCase();
		String clazzLC=clazz.toLowerCase();
		if (!value.contains(clazzLC)) 
			return false;
		
		String[] classes=value.split(" ");
		return Arrays.asList(classes).contains(clazzLC);
	
	}
	
	
	public Query filterAttr(final String name,final String value) {
		return new Query(this,  e -> {Attr a=e.attr(name); return a!=null && a.getValue().equalsIgnoreCase(value);});
	}
	
	public Query filterHasAttr(final String name) {
		return new Query(this,  e -> {Attr a=e.attr(name); return a!=null; });
	}
	
	public Query filterContent(final String value) {
		return new Query(this,  e -> {String c=e.simpleContentString(); return c!=null && c.equals(value);});
	}

	
	public Elem elem() {
		switch(size()) {
		case 0:
			return null;
		case 1:
			return list.get(0);
		default:
			throw new RuntimeException("size="+size());
		}
	}

	public ElemHtml elemHtml() {
		return new ElemHtml(elem());
	}
	
	public Query checkOne() {
		if (size()==1)
			return this;
		throw new RuntimeException("size="+size());
	}
	

	// Deprecato 30/7/19 andrea
	// Usa setText se si tratta di una string o setHtml se si tratta di una stringa con sorgenteHtml
	@Deprecated	
	public Query set(String content) {
		forEach(e -> e.set(content));
		return this;
	}

	public Query setText(String textToEscape) {
		forEach(e -> e.setText(textToEscape));
		return this;
	}
	
	public Query setHtml(String htmlContent) {
		forEach(e -> e.setHtml(htmlContent));
		return this;
	}
	
	public Query set(Part content) {
		if (size()==1)
			elem().set(content);
		else
			forEach(e -> e.set(content.partClone()));
		return this;
	}
	
	public Query set(Attr attr) {
		if (size()==1)
			elem().set(attr);
		else
			forEach(e -> e.set(attr.attrClone()));
		return this;
	}
	
	public Query add(Part content) {
		if (size()==1)
			elem().add(content);
		else
			forEach(e -> e.add(content.partClone()));
		return this;
	}
	
	public Query add(Attr attr) {
		if (size()==1)
			elem().add(attr);
		else
			forEach(e -> e.add(attr.attrClone()));
		return this;
	}
	
	// Deprecato 30/7/19 andrea
	// Usa addText se si tratta di una string o addHtml se si tratta di una stringa con sorgenteHtml
	@Deprecated
	public Query add(String text) {
		forEach(e -> new ElemHtml(e).addHtml(text));
		return this;
	}
	
	public Query addText(String textToEscape) {
		forEach(e -> e.addText(textToEscape));
		return this;
	}

	public Query addHtml(String htmlContent) {
		forEach(e -> e.addHtml(htmlContent));
		return this;
	}

	public Query clearContent() {
		forEach(e -> e.clearContent());
		return this;
	}
	
	public Query remove() {
		forEach(e -> e.remove());
		return this;
	}
	
	public Query replace(Part content) {
		if (size()==1)
			elem().replace(content);
		else
			forEach(e -> e.replace(content.partClone()));
		return this;
	}
	
	@Deprecated //da sostituire con una String di test e non as is
	public Query replace(String content) {
		forEach(e -> e.replace(new TextAsIs(content)));
		return this;
	}
	
	// delegated

	public void forEach(Consumer<? super Elem> action) {
		list.forEach(action);
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	boolean add(Elem e) {
		return list.contains(e) || list.add(e);
	}

	public Elem get(int index) {
		return list.get(index);
	}

	public Iterator<Elem> iterator() {
		return list.iterator();
	}
	
	
	
	
}
