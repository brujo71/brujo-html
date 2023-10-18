package it.brujo.html;


public class TextAsIs extends Text {

	private String content;

	private TextAsIs() {}
	
	public TextAsIs(String content) {
		this.content = content;
	}

	@Override
	protected Part cloneNew() {
		return new TextAsIs();
	}

	@Override
	public String content() {
		return content;
	}

	@Override
	void cloneCopy(Part other) {
		super.cloneCopy(other);
		this.content=((TextAsIs)other).content;
	}
	
	@Override
	public int debug(Writer out,String linePrefix) {
		out.write(linePrefix);
		out.write("TextAsIs len:");
		out.write(String.valueOf(content.length()));
		out.write("\n");
		return 1;
	}

	public String plainText() {
		return Html.HTMLUnescape(content);
	}


}
