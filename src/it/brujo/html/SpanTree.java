package it.brujo.html;

public class SpanTree {

	public static void spanTree(Part p,Consumer partConsumer) {
		spanTree(p, partConsumer, 0);
	}
	
	public static void spanTree(Part p,Consumer partConsumer,int level) {
		partConsumer.consume(p, level);
		if (p instanceof MultiPart && ((MultiPart)p).hasChilds()) {
			for (Part pc:((MultiPart)p).childs()) {
				spanTree(pc, partConsumer, level+1);
			}
		}
	}
	
	
	public interface Consumer {
		public void consume(Part p,int level);
	}
	
}
