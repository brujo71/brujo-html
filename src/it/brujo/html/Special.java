package it.brujo.html;


public class Special extends Part {

	String content;
	char special='?';  // '?' for xml, '%' for jsp
	
	private Special() {
		content=null;
		
	}
	
	public Special(char special,String content) {
		this.special=special;
		if (content==null)
			throw new NullPointerException();
		this.content = content;
	}

	@Override
	public String toString() {
		return "Special [content=" + Html.escapeJS(content) + " special=" + special + "]";
	}

	@Override
	public void write(Writer out) {
		out.write("<");
		out.write(String.valueOf(special));
		out.write(content);
		out.write(String.valueOf(special));
		out.write(">");
	}

	@Override
	public int debug(Writer out,String linePrefix) {
		out.write(linePrefix);
		out.write("special len:");
		out.write(String.valueOf(content.length()));
		out.write(" special:");
		out.write(String.valueOf(special));
		out.write("((("+content+")))");
		out.write("\n");
		return 1;

	}

	@Override
	protected Part cloneNew() {
		return new Special();
	}

	@Override
	void cloneCopy(Part other) {
		super.cloneCopy(other);
		this.content=((Special)other).content;
		this.special=((Special)other).special;
	}
	
	public String contentString() {
		return content;
	}

	@Override
	public int writeLen() {
		return 4+(content==null ? 0:content.length());
	}

	

}
