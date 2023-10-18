package it.brujo.html;


public abstract class Text extends Part {


	
	public static Text creaAsIs(String content) {
		return new TextAsIs(content);
	}
	
	public static Text creaText(String content) {
		return new TextText(content);
	}
	
	@Override
	public String toString() {
		return "Text [content=" + Html.escapeJS(content()) + "]";
	}

	public abstract String content();
	public abstract String plainText();

	@Override
	public void write(Writer out) {
		out.write(content()==null ? "null" : content());
	}

	@Override
	public int writeLen() {
		String c=content();
		return c==null ? 0 : c.length();
	}

}
