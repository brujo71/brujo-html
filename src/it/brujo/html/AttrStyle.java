package it.brujo.html;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AttrStyle extends Attr {

	@Override
	public Attr setValue(String value) {
		Map<String, String> map=parse(value);
		
		return super.setValue(map==null ? value : map2Str(map));
	}

	public final static String nameStyle="style";
	
	AttrStyle() {
		super(nameStyle);
	}

	@Override
	public Attr merge(String value) {
		if (value==null)
			return this;
		Map<String,String> nm=parse(value);
		if (nm!=null) {
			Map<String,String> om=parse(getValue());
			if (om!=null) {
				nm.entrySet().stream().forEach( e -> {
					if (e.getValue()==null) {
						om.remove(e.getKey());
					}
					else {
						om.put(e.getKey(), e.getValue());
					}
				});
				super.setValue(map2Str(om));
				return this;
			}
		}
		
		if (getValue()==null || getValue().trim().length()==0) {
			super.setValue(value);
		}
		
		if (getValue().trim().endsWith(";")) {
			super.merge(value);
		}
		else {
			super.merge("; "+value);
		}
		
		return this;
	}

	
	public static String map2Str(Map<String,String> map) {
		StringBuilder res=new StringBuilder(map.size()*32);
		for (Entry<String, String> me:map.entrySet()) {
			if (me.getValue()!=null) {
				if (res.length()>0)
					res.append(' ');
				res.append(me.getKey());
				res.append(": ");
				res.append(me.getValue());
				res.append(";");			
			}
		}
		return res.toString();
	}
	
	public static Map<String, String> parse(String value) {
		Map<String, String> map=new LinkedHashMap<>();
		if (value!=null) {
			String[] sts=value.split(";");
			for (String st:sts) {
				if (st.trim().length()>0) {
					String[] nv=(st+" ").split(":");
					if (nv.length!=2)
						return null;
					if (nv[0]==null || nv[0].trim().length()==0)
						return null;
					String k=nv[0].trim().toLowerCase();
					if (!styelAttrsSet.contains(k)) {
						return null;
					}
					if (nv[1]!=null && nv[1].trim().length()>0) {
						map.put(k, nv[1].trim());
					}
					else {
						map.put(k, null);
					}
				}
			}
		}
		
		return map;
	}
	
	private final static String[] styelAttrs=new String[] {
			"align-content",
			"align-items",
			"align-self",
			"all",
			"animation",
			"animation-delay",
			"animation-direction",
			"animation-duration",
			"animation-fill-mode",
			"animation-iteration-count",
			"animation-name",
			"animation-play-state",
			"animation-timing-function",
			"backface-visibility",
			"background",
			"background-attachment",
			"background-blend-mode",
			"background-clip",
			"background-color",
			"background-image",
			"background-origin",
			"background-position",
			"background-repeat",
			"background-size",
			"border",
			"border-bottom",
			"border-bottom-color",
			"border-bottom-left-radius",
			"border-bottom-right-radius",
			"border-bottom-style",
			"border-bottom-width",
			"border-collapse",
			"border-color",
			"border-image",
			"border-image-outset",
			"border-image-repeat",
			"border-image-slice",
			"border-image-source",
			"border-image-width",
			"border-left",
			"border-left-color",
			"border-left-style",
			"border-left-width",
			"border-radius",
			"border-right",
			"border-right-color",
			"border-right-style",
			"border-right-width",
			"border-spacing",
			"border-style",
			"border-top",
			"border-top-color",
			"border-top-left-radius",
			"border-top-right-radius",
			"border-top-style",
			"border-top-width",
			"border-width",
			"bottom",
			"box-decoration-break",
			"box-shadow",
			"box-sizing",
			"break-after",
			"break-before",
			"break-inside",
			"caption-side",
			"caret-color",
			"@charset",
			"clear",
			"clip",
			"color",
			"column-count",
			"column-fill",
			"column-gap",
			"column-rule",
			"column-rule-color",
			"column-rule-style",
			"column-rule-width",
			"column-span",
			"column-width",
			"columns",
			"content",
			"counter-increment",
			"counter-reset",
			"cursor",
			"direction",
			"display",
			"empty-cells",
			"filter",
			"flex",
			"flex-basis",
			"flex-direction",
			"flex-flow",
			"flex-grow",
			"flex-shrink",
			"flex-wrap",
			"float",
			"font",
			"@font-face",
			"font-family",
			"font-feature-settings",
			"@font-feature-values",
			"font-kerning",
			"font-language-override",
			"font-size",
			"font-size-adjust",
			"font-stretch",
			"font-style",
			"font-synthesis",
			"font-variant",
			"font-variant-alternates",
			"font-variant-caps",
			"font-variant-east-asian",
			"font-variant-ligatures",
			"font-variant-numeric",
			"font-variant-position",
			"font-weight",
			"grid",
			"grid-area",
			"grid-auto-columns",
			"grid-auto-flow",
			"grid-auto-rows",
			"grid-column",
			"grid-column-end",
			"grid-column-gap",
			"grid-column-start",
			"grid-gap",
			"grid-row",
			"grid-row-end",
			"grid-row-gap",
			"grid-row-start",
			"grid-template",
			"grid-template-areas",
			"grid-template-columns",
			"grid-template-rows",
			"hanging-punctuation",
			"height",
			"hyphens",
			"image-rendering",
			"@import",
			"isolation",
			"justify-content",
			"@keyframes",
			"left",
			"letter-spacing",
			"line-break",
			"line-height",
			"list-style",
			"list-style-image",
			"list-style-position",
			"list-style-type",
			"margin",
			"margin-bottom",
			"margin-left",
			"margin-right",
			"margin-top",
			"max-height",
			"max-width",
			"@media",
			"min-height",
			"min-width",
			"mix-blend-mode",
			"object-fit",
			"object-position",
			"opacity",
			"order",
			"orphans",
			"outline",
			"outline-color",
			"outline-offset",
			"outline-style",
			"outline-width",
			"overflow",
			"overflow-wrap",
			"overflow-x",
			"overflow-y",
			"padding",
			"padding-bottom",
			"padding-left",
			"padding-right",
			"padding-top",
			"page-break-after",
			"page-break-before",
			"page-break-inside",
			"perspective",
			"perspective-origin",
			"pointer-events",
			"position",
			"quotes",
			"resize",
			"right",
			"scroll-behavior",
			"tab-size",
			"table-layout",
			"text-align",
			"text-align-last",
			"text-combine-upright",
			"text-decoration",
			"text-decoration-color",
			"text-decoration-line",
			"text-decoration-style",
			"text-indent",
			"text-justify",
			"text-orientation",
			"text-overflow",
			"text-shadow",
			"text-transform",
			"text-underline-position",
			"top",
			"transform",
			"transform-origin",
			"transform-style",
			"transition",
			"transition-delay",
			"transition-duration",
			"transition-property",
			"transition-timing-function",
			"unicode-bidi",
			"user-select",
			"vertical-align",
			"visibility",
			"white-space",
			"widows",
			"width",
			"word-break",
			"word-spacing",
			"word-wrap",
			"writing-mode",
			"z-index"
	};
	
	private final static Set<String> styelAttrsSet=new HashSet<>();
	
	static {
		for (String s:styelAttrs) {
			styelAttrsSet.add(s);
		}
	}
}
