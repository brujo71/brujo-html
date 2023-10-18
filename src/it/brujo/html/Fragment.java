package it.brujo.html;


public class Fragment extends MultiPart {

	Fragment() {
		super();
	}

	public static Fragment create() {
		return new Fragment();
	}

	@Override
	protected Part cloneNew() {
		return new Fragment();
	}

	@Override
	public int debugHead(Writer out, String linePrefix) {
		out.write(linePrefix);
		out.write("Fragment");
		out.write("\n");
		return 1;
	}
	
	public Fragment add(Part p) {
		super.addContent(p);
		return this;
	}
	
	public Fragment add(Iterable<Part> parts) {
		for (Part p:parts)
			super.addContent(p);
		return this;
	}
	
//	public Fragment add(String content) {
//		super.addContent(new Text(content));
//		return this;
//	}
	
	public Fragment addFirst(Part p) {
		super.addContentFirst(p);
		return this;
	}
	
	
}
