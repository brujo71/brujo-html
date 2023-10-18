package it.brujo.html;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class MultiPart extends Part {

	List<Part> childs=null;

	@Override
	public void write(Writer out) {
		if (childs!=null)
			for (Part p:childs)
				p.write(out);
	}
	
	@Override
	public int writeLen() {
		int res=0;
		if (childs!=null)
			for (Part p:childs)
				res+=p.writeLen();
		return res;
	}

	public void clearContent() {
		childs=null;
	}
	
	void setContent(Part p) {
		clearContent() ;
		addContent(p);
	}
	
	void setContent(Collection<Part> p) {
		clearContent() ;
		addAllContent(p);
	}
	
	
	void addAllContent(Collection<Part> parts){
		if (parts==null)
			return;
		for (Part p:parts) {
			addContent(p);
		}
	}
	
	void addContent(Part p) {
		
		if (childs==null)
			childs=new LinkedList<>();
		
		childs.add(p);
		p.setParent(this);
	}
	
	void addContentFirst(Part p) {
		if (childs==null)
			childs=new LinkedList<>();
		childs.add(0, p);
		p.setParent(this);
	}
	
	public void remove(Part p) {
		if (childs!=null) {
			childs.remove(p);
		}
		p.setParent(null);
	}
	
	public void replace(Part p,Part newp) {
		int idx;
		if (childs!=null && ((idx=childs.indexOf(p))>=0) ) {
			childs.set(idx, newp);
			newp.setParent(this);
		}
		else {
			throw new ModificationException(this, "no child for "+p);
		}
		p.setParent(null);
	}
	
//	void addAll(Collection<Part> parts) {
//		if (parts==null)
//			return;
//		for (Part p:parts) {
//			addContent(p);
//		}
//	}
	
	public List<Part> childs() {
		return childs;
	}

	public void walkElemTree(Consumer<Elem> doSomething) {
		walkElemTree(doSomething, Elem -> {return true;});
	}
	
	public void walkElemTree(Consumer<Elem> doSomething,Predicate<Elem> filter) {
		if (this instanceof Elem && filter.test((Elem)this))
			doSomething.accept((Elem)this);
		if (childs()!=null) {
			for (Part p:childs()) {
				if (p instanceof MultiPart  )
					((MultiPart)p).walkElemTree(doSomething,filter);
			}
		}
	}

	@Override
	void cloneCopy(Part other) {
		super.cloneCopy(other);
		MultiPart omp=(MultiPart)other;
		if (omp.childs!=null) {
			for (Part p:omp.childs)  {
				addContent(p.partClone());
			}
		}
	}
	
	public int size() {
		return childs()==null ? 0 : childs().size();
	}

	@Override
	protected Part cloneNew() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChilds() {
		return size()>0;
	}
	
	/**
	 * Ritorna il testo contenuto nel {@code MultiPart} solo se contiene esclusivamente testo semplice ({@code Text}). Altrimenti ritorna {@code null}.<br>
	 * Esempi:<br>
	 * {@code <div>Hello world</div>}              -> {@code "Hello world"}<br>
	 * {@code <div>Hello <span>world</span></div>} -> {@code null}
	 * @return {@code null} se il {@code MultiPart} è vuoto o se contiene sotto-{@code Part} non di tipo {@code Text}
	 */
	public String simpleContentString() {
		if (hasChilds()) {
			StringBuilder res=new StringBuilder();
			for (Part p: childs()) {
				if (p instanceof Text) {
					String str=((Text)p).plainText();
					if (str!=null) 
						res.append(str);
				}
				else {
					return null;
				}
			}
			return res.toString().trim();
		}
		return null;
 	}
	
	/**
	 * Ritorna il testo contenuto nel {@code MultiPart}, compresi i testi contenuti in eventuali sotto-{@code Part} annidati.<br>
	 * Esempio:<br>
	 * {@code <div>Hello <span>world</span></div>} -> {@code "Hello world"}
	 * @return {@code null} se il {@code MultiPart} è vuoto
	 */
	public String contentString() {
		return contentString(false);
	}
	
	/**
	 * {@code onlydeeper}=<b>{@code false}</b>:<br>
	 * ritorna il testo contenuto nel {@code MultiPart}, compresi i testi contenuti in eventuali sotto-{@code Part} annidati. Esempio:<br>
	 * {@code <div>Hello <span>world</span></div>} -> {@code "Hello world"}<br>
	 * <br>
	 * {@code onlydeeper}=<b>{@code true}</b>:<br>
	 * ritorna solo il testo contenuto nel {@code Part} più annidato. Esempio:<br>
	 * {@code <div>Hello <span>world</span></div>} -> {@code "world"}
	 * @param onlydeeper determina il comportamento come descritto sopra
	 * @return {@code null} se il {@code MultiPart} è vuoto
	 */
	public String contentString(boolean onlydeeper) {
		if (hasChilds()) {
			StringBuilder res=new StringBuilder();
			for (Part p: childs()) {
				if (!onlydeeper && p instanceof Text) {
					String str=((Text)p).content();
					if (str!=null) 
						res.append(str);
				}
				else if (p instanceof Elem) {
					if (((Elem)p).nameLC().equals("br")) {
						res.append('\n');
					}
					else {
						String str=((Elem)p).contentString();
						if (str!=null) 
							res.append(str);
					}
				}
			}
			return res.toString().trim();
		}
		return null;
 	}
	
	public abstract int debugHead(Writer out,String linePrefix);
	@Override
	public final int debug(Writer out,String linePrefix) {
		int res=0;
		res+=debugHead(out,linePrefix);
		if (childs!=null)
			for (Part p:childs)
				res+=p.debug(out,linePrefix+"  ");
		
		if (res>10)
			res+=debugHead(out,linePrefix+"/");
		
		return res;
	}
	
}
