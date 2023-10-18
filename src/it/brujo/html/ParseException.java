package it.brujo.html;


public class ParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParseException() {
		// TODO Auto-generated constructor stub
	}

	public static ParseException fromBuf(String html,int pos,ParseException pe) {
		int startPos=Math.max(0, pos-20);
		int stopPos=Math.min(pos, html.length());
		String message=html.substring(startPos, stopPos);
		
		return new ParseException(message,pe);
	}

	public ParseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ParseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ParseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
