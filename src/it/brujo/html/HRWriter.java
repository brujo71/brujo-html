package it.brujo.html;

import it.brujo.html.Elem.Closure;

public class HRWriter {
	
	private Writer out;
	
	
	public HRWriter(Writer out) {
		this.out=out;
	}

	public void write(Part p) {
		consume(p, 0);
	}
	
	private void spaces(int level) {
		for (int i=level;i>0;i--)
			out.write("  ");
	}
	
	private boolean checkIsToSplit(Part p) {
		if (p instanceof Elem) {
			Elem e=(Elem)p;
			if (e.hasChilds()) {
				 if (p.writeLen()>80) {
					 boolean complex=false;
					 for (Part pc:e.childs()) {
						 if (pc instanceof MultiPart) {
							 complex=true;
							 break;
						 }
					 }
					 return complex;
				 }
			}
		}
		return false;
	}
	
	private void consume(Part p,int level) {
		
		if (checkIsToSplit(p)) {
			Elem e=(Elem)p;
//			int len=e.writeLen();
			 spaces(level);
			 e.writeTag(out);
			 out.write("\n");
			
			if (e.childs!=null)
				for (Part pp:e.childs) {
					consume(pp, level+1);
				}
			
			if (e.getClosure() == Closure.EXPLICIT) {
				 spaces(level);
				e.writeTagClosure(out);
				 out.write("\n");
			}
				
		}
		else if (p instanceof Fragment){
			MultiPart e=(MultiPart)p;
			if (e.childs!=null)
				for (Part pp:e.childs) {
					consume(pp, level+1);
				}
		}

		else {
			spaces(level);
			p.write(out);
			out.write("\n");
		}
		
		
	}
	
}
