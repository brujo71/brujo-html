package it.brujo.html;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class Html {
		

	/** 
	 * 13/6/17 non ho deprecato perché troppo diffuso ma meglio usare EscapeJS 
	 * @param s {@code String}
	 * @return {@code String} {@code String} 
	 **/
	public static String escapeJS(String s) {
		return EscapeJS.escapeString(s);
//		return s==null ? null : s.replaceAll("'", "\\\\'").replaceAll("\n","\\\\n").replaceAll("\r","\\\\r").replaceAll("\"", "\\\\\"");
	}

	/** 
	 * 13/6/17 non ho deprecato perché troppo diffuso ma meglio usare EscapeJS 
	 *@param s {@code String}
	 *@return {@code String} {@code String}
	 **/
	public static String escapeQuoteJS(String s) {
		return EscapeJS.escQuoteString(s);
//		return s==null ? "null" : "'"+escapeJS(s)+"'";
	}

	
	public static String HTMLEscapeHref(String s) {
		return HTMLEscape(s);
	}
	
	
	private final static Map<String,Character> htmlUnescMap = new HashMap<>();
	private final static Map<Character,String> htmlEscMap = new HashMap<>();
	private final static Map<Character,String> htmlEscFullMap = new HashMap<>();
	
	static {
		htmlUnescMap.put("quot", '\"');
		htmlUnescMap.put("gt", '>');
		htmlUnescMap.put("lt", '<');
		htmlUnescMap.put("amp", '&');
		
		htmlUnescMap.forEach((k,v) -> htmlEscMap.put(v,k));

		htmlUnescMap.put("rsquo", '’');
		htmlUnescMap.put("euro", '€');
		htmlUnescMap.put("szlig", 'ß');
		htmlUnescMap.put("aelig", 'æ');
		htmlUnescMap.put("Agrave", 'À');
		htmlUnescMap.put("Acirc", 'Â');
		htmlUnescMap.put("Auml", 'Ä');
		htmlUnescMap.put("Egrave", 'È');
		htmlUnescMap.put("Eacute", 'É');
		htmlUnescMap.put("Ecirc", 'Ê');
		htmlUnescMap.put("Igrave", 'Ì');
		htmlUnescMap.put("Ograve", 'Ò');
		htmlUnescMap.put("Ouml", 'Ö');
		htmlUnescMap.put("Uuml", 'Ü');
		htmlUnescMap.put("agrave", 'à');
		htmlUnescMap.put("aacute", 'á');
		htmlUnescMap.put("acirc", 'â');
		htmlUnescMap.put("auml", 'ä');
		htmlUnescMap.put("egrave", 'è');
		htmlUnescMap.put("eacute", 'é');
		htmlUnescMap.put("ecirc", 'ê');
		htmlUnescMap.put("igrave", 'ì');
		htmlUnescMap.put("ograve", 'ò');
		htmlUnescMap.put("ouml", 'ö');
		htmlUnescMap.put("ugrave", 'ù');
		htmlUnescMap.put("uuml", 'ü');
		
		htmlUnescMap.forEach((k,v) -> htmlEscFullMap.put(v,k));

		htmlUnescMap.put("nbsp", ' ');
		
		htmlEscFullMap.put('\'',"rsquo");
		htmlEscFullMap.put('“',"quot");
		htmlEscFullMap.put('”',"quot");
	}
	
	public static String HTMLUnescape(String htmlText) {
		return HTMLUnescape(htmlText, null);
	}
	
	public static String HTMLUnescape(String htmlText,Consumer<String> notHandled) {
		if (htmlText==null) return "";
		StringBuffer res=new StringBuffer(htmlText.length());
		boolean stateNormal=true;
		CharBuffer htmlBuf=CharBuffer.wrap(htmlText).asReadOnlyBuffer();
		StringBuilder escSec=new StringBuilder(10);
		while (htmlBuf.hasRemaining()) {
			char c=htmlBuf.get();
			if (stateNormal) {
				if (c=='&') {
					stateNormal=false;
				}
				else {
					res.append(c);
				}
			}
			else {
				if (c==';') {
					stateNormal=true;
					boolean done=false;
					if (escSec.length()>0) {
						if (escSec.charAt(0)=='#') {
							int code=0;
							try {
								if (escSec.charAt(1)=='x' || escSec.charAt(1)=='X') 
									code=Integer.parseInt(escSec.substring(2),16);
								else
									code=Integer.parseInt(escSec.substring(1));
							}
							catch (Exception e) {
								code=0;
							}
							if (code>0) {
								res.appendCodePoint(code);
								done=true;
							}
						}
						else {
							Character esc=htmlUnescMap.get(escSec.toString());
							if (esc!=null) {
								res.append(esc);
								done=true;
							}
						}
					}
					if (!done) {
						res.append('&');
						res.append(escSec);
						res.append(';');
						if (notHandled!=null) {
							notHandled.accept(escSec.toString());
						}
					}
					escSec.setLength(0);
				}
				else {
					if (escSec.length()<10) {
						escSec.append(c);
					}
					else {
						stateNormal=true;
						res.append('&');
						res.append(escSec);
						res.append(c);
						escSec.setLength(0);
					}
				}
			}
		} //while
		if (!stateNormal) {
			res.append('&');
			res.append(escSec);
		}
		
		return res.toString();
	}
	
	/**
	 * html escape
	 * @param s {@code String}
	 * @return {@code String} {@code String} 
	 **/
	public static String HTMLEscape(String s) {
		return HTMLEscape(s,false,false);
	}

	/**
	 *html escape NL
	 *@param s {@code String}
	 *@return {@code String} {@code String} 
	 **/
	public static String HTMLEscapeNL(String s) {
		return HTMLEscape(s,true,false);
	}

	private static String HTMLEscape(String s,boolean nl,boolean full) {
		var map=full ? htmlEscFullMap : htmlEscMap;
		if (s==null) return "";
		StringBuffer res=new StringBuffer(s.length()*11/10+32);
		for (int i=0; i<s.length() ; i++ ) 		{
			char c=s.charAt(i);
			String escStr=map.get(c);
			if (escStr==null) {
				if(nl && c=='\n')
						res.append("<br />\n");
				else if(nl && c=='\r')
					;
				else if(full && Character.codePointAt(s.toCharArray(), i)>127)
					res.append("&#x"+Integer.toHexString(Character.codePointAt(s.toCharArray(), i))+";");
				else
					res.append(c);
			}
			else  {
				res.append('&');
				res.append(escStr);
				res.append(';');
			}
				
		}
		return res.toString();
	}//HTMLEscape

	public static String HTMLEscapeFull(String s) {
		return HTMLEscape(s, true, true);
	}
	
	
	 static String HTMLEscapeFull_OLD_(String s) {
		if (s==null) return "";
		StringBuffer res=new StringBuffer();
		for (int i=0; i<s.length() ; i++ )
		{
			char c=s.charAt(i);
			if (c=='"' || c=='“' || c=='”')
				res.append("&quot;");
			else if(c=='&')
				res.append("&amp;");
			else if(c=='>')
				res.append("&gt;");
			else if(c=='<')
				res.append("&lt;");
			else if(c=='\'' || c=='’')
				res.append("&rsquo;");
			else if(c=='\r')
				;
			else if(c=='\n')
				res.append("<br />\n");
			else if(c=='…')
				res.append("...");
			else if(c=='à')
				res.append("&agrave;");
			else if(c=='è')
				res.append("&egrave;");
			else if(c=='ì')
				res.append("&igrave;");
			else if(c=='ò')
				res.append("&ograve;");
			else if(c=='ù')
				res.append("&ugrave;");

			else if(c=='á')
				res.append("&aacute;");
			else if(c=='é')
				res.append("&eacute;");
			else if(c=='í')
				res.append("&iacute;");
			else if(c=='ó')
				res.append("&oacute;");
			else if(c=='ú')
				res.append("&uacute;");

			else if(c=='À')
				res.append("&Agrave;");
			else if(c=='È')
				res.append("&Egrave;");
			else if(c=='Ì')
				res.append("&Igrave;");
			else if(c=='Ò')
				res.append("&Ograve;");
			else if(c=='Ù')
				res.append("&Ugrave;");

			else if(c=='Á')
				res.append("&Aacute;");
			else if(c=='É')
				res.append("&Eacute;");
			else if(c=='Í')
				res.append("&Iacute;");
			else if(c=='Ó')
				res.append("&Oacute;");
			else if(c=='Ú')
				res.append("&Uacute;");

			else if(c=='ä')
				res.append("&auml;");
			else if(c=='ë')
				res.append("&euml;");
			else if(c=='ï')
				res.append("&iuml;");
			else if(c=='ö')
				res.append("&ouml;");
			else if(c=='ü')
				res.append("&uuml;");
			
			else if(c=='Ä')
				res.append("&Auml;");
			else if(c=='Ë')
				res.append("&Euml;");
			else if(c=='Ï')
				res.append("&Iuml;");
			else if(c=='Ö')
				res.append("&Ouml;");
			else if(c=='Ü')
				res.append("&Uuml;");
			
			else if(c=='ß')
				res.append("&szlig;");
			else if(c=='€')
				res.append("&euro;");
			else if(c=='č')
				res.append("&#x10d;");
			else if(c=='š')
				res.append("&#x161;");

			else 
				res.append(c);
			
		}
		return res.toString();
	}

	
}
