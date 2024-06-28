package com.xlbit.lib.js;

public class CreateJS {
	
	
	public static final String var(String name, Object value) {
		return var(name, value, false);
	}

	public static final String varGlobal(String name, Object value) {
		return var(name, value, true);
	}

	private static final String var(String name, Object value, boolean global) {
		StringBuilder sb = new StringBuilder(32);
		if (!global) {
			sb.append("var ");
		}
		sb.append(name);
		sb.append(" = ");

		if (value == null) {
			sb.append("null");
		}
		else if (value instanceof String) {
			sb.append(EscapeJS.escQuoteString((String) value));
		}
		else if (value instanceof Integer) {
			sb.append(String.valueOf(value));
		}
		else if (value instanceof Boolean) {
			sb.append(String.valueOf(value));
		}
//		else if (value instanceof JSonElem) {
//			sb.append("JSON.parse(");
//			sb.append(EscapeJS.escQuoteString(Json.writeToString((JSonElem) value)));
//			sb.append(")");
//		}
		else {
			throw new IllegalArgumentException("not supported " + value.getClass().getName());
		}

		sb.append(";\n");
		return sb.toString();
	}
	
//	public static String text2js(String multiLineString) {
//
//		List<String> lines = StringUtil.splitLines(multiLineString);
//		WriterString res = new WriterString(multiLineString.length() + 512);
//		res.write("[\n");
//
//		for (int i = 0; i < lines.size(); i++) {
//			if (i > 0)
//				res.write(",\n");
//			res.write(Html.escapeQuoteJS(lines.get(i)));
//		}
//		res.write("\n].join('')");
//
//		return res.out();
//	}
	
	
//	private final static String jsCodeStart="{{";
//	private final static String jsCodeEnd="}}";
//	private final static String jsCodeStartQuot=Pattern.quote(jsCodeStart);
//	private final static String jsCodeEndQuot=Pattern.quote(jsCodeEnd);
//	
//	public static String text2jsCode(String source) {
//		StringTokenizer tok = new StringTokenizer(source, jsCodeStart);
//
//		while (tok.hasMoreElements()) {
//
//		}
//		return null;
//	}
	

	
}
