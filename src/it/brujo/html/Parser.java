package it.brujo.html;

import java.nio.CharBuffer;
import java.util.LinkedList;

import it.brujo.html.Elem.Closure;


public class Parser {

	static enum Mode {Html,ScriptBody};
	
	Mode mode=Mode.Html;
	
	
	LinkedList<Part> res=new LinkedList<Part>();
	public Parser() {
	}

	CharBuffer htmlBuf=null;
	int line=1;
	
	public static Fragment parseHtml(String html) {
		return new Parser().parse(html);
	}
	
	public Fragment parse(String html) {
		try {
			parseInner(html);
			 Fragment f= Fragment.create();
			 f.addAllContent(res);
			return f;
		}
		catch (ParseException pe) {
			
			throw ParseException.fromBuf(html, htmlBuf.position(),pe);
		}
	}
	
	private void parseInner(String html) {
		res.clear();
		htmlBuf=CharBuffer.wrap(html).asReadOnlyBuffer();
		line=1;
		while (htmlBuf.hasRemaining()) {
			
			Part e=parseElem();
			if (e!=null)
				res.add(e);
			String text=getText();
			if (text!=null) {
				String textText=null;
				if (mode==Mode.Html) {
					try {
						textText=Html.HTMLUnescape(text);
						if (!Html.HTMLEscapeFull(textText).equals(text) ) {
							textText=null;
						}
					}
					catch (Exception ee) {
						
					}
				}
				if (textText==null) 
					res.add(Text.creaAsIs(text));
				else
					res.add(Text.creaText(textText));
			}
			mode=Mode.Html;
			if (e==null && text==null)
				throw new ParseException();
		}
		
	}
	
	private String getText() {
		return mode==Mode.ScriptBody ? getScriptBody() : getText("<",-1);
	}

	private boolean compare(String match) {
		int pos=htmlBuf.position();
		boolean res=true;
		for (int i=0;i<match.length();i++) {
			if (!htmlBuf.hasRemaining() || match.charAt(i)!=htmlBuf.get()) {
				res=false;
				break;
			}
		}
		
		htmlBuf.position(pos);
		return res;
	}
	
	private String getScriptBody() {
		StringBuilder res=new StringBuilder();
		while (htmlBuf.hasRemaining() && !compare("</script>")) {
			res.append(htmlBuf.get());
		}
		return res.length()==0 ? null : res.toString();
	}

	private String getText(String terminators, int limit) {
		StringBuilder res=null;
		while (htmlBuf.hasRemaining()) {
			char c=get();
			if (terminators.indexOf(c)>=0) {
				oneback();
				break;
			}
			if (res==null)
				res=new StringBuilder();
			res.append(c);
			if (limit>0 && res.length()>limit)
				throw new ParseException();
		}
		return res==null ? null : res.toString();
	}

	private void skip() {
		while (" \t\n\r".indexOf(get())>=0);
		oneback();
	}
	
	private void oneback() {
		htmlBuf.position(htmlBuf.position()-1);
	}
	
	private char get() {
		char c=htmlBuf.get();
		if (c=='\n')
			line++;
		return c;
	}
	
	private void paerseAttrs(Elem e) {
		skip();
		for (String name=getText("= />\"", 50); name!=null; name=getText("= />\"", 50)) {
			Attr attr=Attr.create(name);
			e.add(attr);
			skip();
			char c=get();
			if (c=='=') {
				skip();
				char apice=get();
				String bound=null;
				if (apice=='"' || apice=='\'') {
					bound=""+apice;
				} else if (apice=='_' ||  Character.isLetter(apice)) {
					bound="\"' ";
					apice=' ';
				}
				else {
					throw new ParseException();
				}
				String value=getText(bound, 5000);
				attr.setValue(value==null ? "" : value);
				c=get();
				if (apice!=' ' && c!=apice) {
					throw new ParseException();
				}
				skip();
			}
			else {
				oneback();
			}
		}
		return ;
	}
	
	private Part parseElemClosure() {
		String name=getText("\n\r\t >",50);
		skip();
		if (get()!='>') {
			throw new ParseException();
		}
		LinkedList<Part> contained=new LinkedList<Part>();
		Part p=res.pollLast();
		while (p!=null && !(p instanceof Elem  && ((Elem)p).closeMatch(name)) ) {
			contained.addFirst(p);	
			 p=res.pollLast();
		}
		if (p==null) {
//			logWarning("p==null contained.size="+contained.size());
			p=new Elem("unknown");
		}
		Elem closed=(Elem)p;
		closed.addAllContent(contained);
		closed.setToLine(line);
		closed.setClosure(Closure.EXPLICIT);
		return closed;		
	}
	

	
	private Part parseElemComment() {
		int numOfDash=0;
		while (get()=='-') {
			numOfDash++;
		}
		oneback();
			
		StringBuilder comm=new StringBuilder();
		int numOfClosingDash=0;

		commentLoop:
		while (htmlBuf.hasRemaining()) {
			char cc=get();
			if (cc=='>') {
				if (numOfDash==0) {
					oneback();
					break commentLoop;
				}
//				else {
//					comm.append(cc);
//					cc=get();
//				}	
			}
			else if (cc=='-') {
				numOfClosingDash=1;
				while (get()=='-')
					numOfClosingDash++;
				oneback();
				if (get()=='>') {
					if (numOfClosingDash>=numOfDash) {
						oneback();
						break commentLoop;
					}
					else {
						int back=numOfClosingDash;
						while (back-->0)  {
							oneback();
						}
						numOfClosingDash--;
						while (numOfClosingDash-->0)  {
							comm.append(cc);
							cc=get();
						}						
					}
				}
				else {
//					throw new RuntimeException("dash dash");
					int back=numOfClosingDash;
					while (back-->0)  {
						oneback();
					}
					numOfClosingDash--;
					while (numOfClosingDash-->0)  {
						comm.append(cc);
						cc=get();
					}
				}
			}
			
			comm.append(cc);
		}
		char ccc;
		if ((ccc=get())!='>')
			throw new RuntimeException("ccc="+ccc);

		Comment res=new Comment(comm.toString());

		res.numberOfDash=numOfDash;
		return res;
	}
	
	private Part parseElemSpecial() {
		oneback();
			
		char special=get();
		
		StringBuilder comm=new StringBuilder();

		commentLoop:
		while (htmlBuf.hasRemaining()) {
			char cc=get();
			if (cc==special) {
				if (get()=='>') {
					oneback();
					break commentLoop;
				}
				else {
					oneback();
				}
			}
			
			comm.append(cc);
		}
		char ccc;
		if ((ccc=get())!='>')
			throw new RuntimeException("ccc="+ccc);
		
		Special res= new Special(special,comm.toString());
		
		return res;
	}
	

	private Part parseElem() {
		char c=get();
		if (c!='<') {
			oneback();
			return null;
		}
		c=get();
		if (c=='/') {
			return parseElemClosure();
		}
		else if (c=='!') {
			return parseElemComment();
		}
		else if ( c=='?' || c=='%') {
			return parseElemSpecial();
		}
		else {
			oneback();
		}
		String name=getText(" />\n\t\r",150);
		if (name==null)
			throw new ParseException();
		Elem res=Elem.create(name);
		res.setFromLine(line);
		paerseAttrs(res);
		c=get();
		boolean open=true;
		if (c=='/') {
			open=false;
			c=get();
		}
		if (c!='>') {
			throw new ParseException();
		}
		if (!open) {
			res.setClosure(Closure.COMPACT);
		}
		if (open && res.name().equals("script")) {
			mode=Mode.ScriptBody;
		}
		res.setToLine(line);
		return res;
	}
	
	
//	public void dempRes() {
//		for (Part p: res) {
//			logInfo(p.toString());
//		}
//	}

//	public void logDebug(String mess, Throwable e) {
//		if (log!=null)
//			log.logDebug(mess, e);
//	}
//
//	public void logDebug(String mess) {
//		if (log!=null)
//			log.logDebug(mess);
//	}
//
//	public void logInfo(String mess, Throwable e) {
//		if (log!=null)
//			log.logInfo(mess, e);
//	}
//
//	public void logInfo(String mess) {
//		if (log!=null)
//			log.logInfo(mess);
//	}
//
//	public void logWarning(String mess, Throwable e) {
//		if (log!=null)
//			log.logWarning(mess, e);
//	}
//
//	public void logWarning(String mess) {
//		if (log!=null)
//			log.logWarning(mess);
//	}
//
//	public void logSevere(String mess, Throwable e) {
//		if (log!=null)
//			log.logSevere(mess, e);
//	}
//
//	public void logSevere(String mess) {
//		if (log!=null)
//			log.logSevere(mess);
//	}
//
//	
	
	
}
