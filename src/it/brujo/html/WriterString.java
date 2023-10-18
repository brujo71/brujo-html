package it.brujo.html;

import java.util.function.Consumer;

public class WriterString implements Writer {

	StringBuilder out;
	
	public WriterString() {
		out=new StringBuilder();
	}

	public WriterString(int capacity) {
		out=new StringBuilder(capacity);
	}

	public void write(String s) {
		out.append(s);
	}
	
	public String out() {
		return out.toString();
	}

	public static String writeToString(Consumer<Writer> writable) {
		WriterString out=new WriterString();
		writable.accept(out);
		return out.out();
	}
	
}
