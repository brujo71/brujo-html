package it.brujo.html;


public class EscapeJS {

	public static String escapeString(String s) {
		return escapeString(s, null, false);
	}
	
	/**Se doQuote è false e quotingChar è null vengono escappati sia l'apice singolo che 
	 * quello doppio.
	 * 
	 * @param s la string da escappare
	 * @param quotingChar in genere o &quot; o &#39; 
	 * 			(se doQuote è false influisce solo sui caratteri che vengono escappati, 
	 * 			se doQuote e true viene usate per racchiudere la stringa)
	 * @param doQuote se true aggiunge i quote esterni (se quotingChar è null usa &#39;)
	 * @return la stringa usabile in javascript
	 */
	public static String escapeString(String s,Character quotingChar,boolean doQuote) {
		if (s==null) return doQuote ? "null" : null;
		StringBuilder res=new StringBuilder(64+s.length()*11/10);
		if (doQuote) {
			if (quotingChar==null)
				quotingChar='\'';
			res.append(quotingChar);
		}
		
		final Character qc=quotingChar;
		s.chars().forEach(c -> {
			if (c=='\'' && (qc==null || qc=='\'')) {
				res.append("\\'");
			}
			else if (c=='"' && (qc==null || qc=='"')) {
				res.append("\\\"");
			}
			else if (c=='\\') {
				res.append("\\\\");
			}
			else if (c=='\r') {
				res.append("\\r");
			}
			else if (c=='\n') {
				res.append("\\n");
			}
			else if (c=='\t') {
				res.append("\\t");
			}
			else {
				res.append(c);
			}
		});
		
		if (doQuote) {
			res.append(qc);
		}
		return res.toString();
	}

	public static String escQuoteString(String s) {
		return escapeString(s,'\'',true);
	}
	
	


}
