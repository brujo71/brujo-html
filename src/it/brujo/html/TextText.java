package it.brujo.html;


public class TextText extends Text {

	private String textCont=null;
	
	private TextText() {}
	
	TextText(String textCont) {
		this.textCont=textCont;
	}
	
	@Override
	public String content() {
		return Html.HTMLEscapeFull(textCont);
	}

	@Override
	public int debug(Writer out,String linePrefix) {
		out.write(linePrefix);
		out.write("TextText len:");
		out.write(String.valueOf(textCont.length()));
		out.write("\n");
		return 1;
	}
	

	@Override
	protected Part cloneNew() {
		return new TextText();
	}

	@Override
	void cloneCopy(Part other) {
		super.cloneCopy(other);
		this.textCont=((TextText)other).textCont;
	}

	public String plainText() {
		return textCont;
	}

}
