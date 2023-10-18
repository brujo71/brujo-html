package it.brujo.html;


public abstract class Part implements Writable {
	
	Part parent=null;
	int fromLine=0,toLine=0;
	
	public abstract void write(Writer out);
	public abstract int debug(Writer out,String linePrefix);
	public abstract int writeLen();
	
	public String writeToString() {
		WriterString ws=new WriterString();
		write(ws);
		return ws.out();
	}

	public int getFromLine() {
		return fromLine;
	}

	public void setFromLine(int fromLine) {
		this.fromLine = fromLine;
	}

	public int getToLine() {
		return toLine;
	}

	public void setToLine(int toLine) {
		this.toLine = toLine;
	}

	void setParent(Part p) {
		this.parent=p;
	}
	
	final public Part partClone() {
		Part res=cloneNew();
		res.cloneCopy(this);
		return res;
	}
	
	protected abstract Part cloneNew();
	
	void cloneCopy(Part other) {
		fromLine=other.fromLine;
		toLine=other.toLine;
	}
	
	public Part parent() {
		return parent;
	}
	
	@SuppressWarnings("unchecked")
	public<P extends Part> P addTo(MultiPart e) {
		e.addContent(this);
		return (P)this;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+" [parent=" + parent + ", fromLine=" + fromLine
				+ ", toLine=" + toLine + "]";
	}
	
	
}
