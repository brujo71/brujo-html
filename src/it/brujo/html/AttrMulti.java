package it.brujo.html;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class AttrMulti extends Attr {

	String sep;
	String sepQuoted; //for regexp
	LinkedList<String> values=new LinkedList<>();
	
	AttrMulti(String name,String sep) {
		super(name);
		this.sep=sep;
		this.sepQuoted=Pattern.quote(sep);
	}

	

	@Override
	public Attr setValue(String value) {
		setValue(null);
		return merge(value);
	}

	@Override
	public Attr merge(String value) {
		if (value==null || value.length()==0)
			return this;
		String[] vs=value.split(sepQuoted);
		for (String v:vs) {
			values.add(v);
			if (getValue()==null || getValue().length()==0) 
				setValue(v);
			else
				setValue(getValue()+sep+v);
		}
		return this;
	}

	
	
}
