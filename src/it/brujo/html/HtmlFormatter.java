package it.brujo.html;


import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.function.Function;



public class HtmlFormatter {

	private Part frag;
	private LinkedList<Elem> elems=new LinkedList<>();
	private LinkedList<Attr> attrs=new LinkedList<>();
	
	private HtmlFormatter(Part frag) {
		this.frag=frag;
	}
	
	private boolean checkTag(String content) {
		return content!=null && content.startsWith("{") && content.endsWith("}") && content.length()>2;
	}
	
	private void parseElem(Part p,int lev) {
		if (p instanceof Elem) {
			Elem e=(Elem)p;
			if (e.attrs()!=null) {
				for (Attr a:e.attrs()) {
					if (checkTag(a.getValue())) {
						attrs.add(a);
					}
				}
			}
			
			
			if (e.hasChilds() && e.childs().size()==1) {
				Part cp=e.childs().get(0);
				if (cp instanceof Text) {
					String cpc=((Text)cp).content();
					if (checkTag(cpc)) {
						elems.add(e);
					}
				}
			}
		}
	}
	
	private void parse() {
		SpanTree.spanTree(frag, this::parseElem);
	}
	
	private String removeBrakets(String cpc) {
		return cpc.substring(1, cpc.length()-1);
	}
	
	private String elabExpr(Function<String, String> map,String expr) {
		if (expr.indexOf('{')>=0) {
			StringBuilder res=new StringBuilder(expr.length()*2+32);
			StringBuilder value=new StringBuilder(64);

			boolean textmode=true;
			
			for (int i=0;i<expr.length();i++) {
				char c=expr.charAt(i);
				if (textmode) {
					if (c=='{') {
						textmode=false;
					}
					else {
						res.append(c);
					}
				}
				else {
					if (c=='}') {
						textmode=true;
						res.append(map.apply(value.toString()));
						value.setLength(0);
					}
					else {
						value.append(c);
					}
				}
			}
			
			
			
			return res.toString();
		}
		else {
			return map.apply(expr);
		}
	}
	
	
	private void format(Function<String, String> map) {
		
		parse();
		
		for (Elem e:elems) {
			Part cp=e.childs().get(0);
			if (cp instanceof Text) {
				String cpc=((Text)cp).content();
				if (cpc!=null && cpc.startsWith("{") && cpc.endsWith("}") && cpc.length()>2) {
					String newContent=elabExpr(map,removeBrakets(cpc));
					if (e.attr("data-htj-replace")!=null) {
						e.replace(new TextAsIs(newContent));
					}
					else {
						e.set(newContent);
					}
				}
				else {
					throw new RuntimeException("e="+e.toString());
				}
			}
			else {
				throw new RuntimeException("e="+e.toString());
			}
		}
		for (Attr a:attrs) {
			String tag=removeBrakets(a.getValue());
			a.setValue(elabExpr(map,tag));
		}
	}
	
	
	public static Part format(Part frag,Function<String, String> map) {
		HtmlFormatter hf=new HtmlFormatter(frag);
		hf.format(map);
		return hf.frag;
	}
	

	
	public static class MapObject implements Function<String, String>  {
		
		private Object obj;
		private boolean debug=true;
		
		public MapObject(Object obj) {
			super();
			this.obj = obj;
		}

		public String apply(String t) {
	
			Method m=null;
			Object resObj=null;
			String res=null;
			
			try {
				try { 
					m= obj.getClass().getMethod(t);
				}
				catch (NoSuchMethodException nsm) {
					throw new Exception("no method "+t+" in class "+obj.getClass());
				}

				resObj=m.invoke(obj);
				
				if (resObj==null) {
					throw new Exception("NULL for "+t);
				}

				res=resObj.toString();
			}
			catch (Exception e) {
				if (debug) {
					res="ERROR "+e.toString();
				}
				else {
					throw new RuntimeException(e);
				}
			}
			return res;
		}
	}
	
}
