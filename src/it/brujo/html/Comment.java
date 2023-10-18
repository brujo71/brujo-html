package it.brujo.html;


public class Comment extends Part {

	String comment;
	int numberOfDash=2;  // 0: "<!" 1: "<!-" 2: "<!--" 

	private Comment() {
		comment=null;
	}
	
	public Comment(String comment) {
		if (comment==null)
			throw new NullPointerException();
		if (comment.indexOf("-->")>=0)
			throw new IllegalArgumentException("comment cannot contain '-->'");
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Comment [content=" + Html.escapeJS(comment) + "]";
	}

	@Override
	public void write(Writer out) {
		out.write("<!");
		for (int i=0;i<numberOfDash;i++)
			out.write("-");
		out.write(comment);
		for (int i=0;i<numberOfDash;i++)
			out.write("-");
		out.write(">");
	}

	@Override
	public int debug(Writer out,String linePrefix) {
		out.write(linePrefix);
		out.write("comment len:");
		out.write(String.valueOf(comment.length()));
		out.write(" numOfDash:");
		out.write(String.valueOf(numberOfDash));
		out.write("((("+comment+")))");
		out.write("\n");
		return 1;

	}

	@Override
	protected Part cloneNew() {
		return new Comment();
	}

	@Override
	void cloneCopy(Part other) {
		super.cloneCopy(other);
		this.comment=((Comment)other).comment;
		this.numberOfDash=((Comment)other).numberOfDash;
	}
	
	public String contentString() {
		return comment;
	}

	@Override
	public int writeLen() {
		return 3+(2*numberOfDash)+(comment==null ? 0:comment.length());
	}

	

}
