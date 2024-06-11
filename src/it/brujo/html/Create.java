package it.brujo.html;

import it.brujo.html.Elem.Closure;

public class Create {

	public static Fragment fragment() {
		return new Fragment();
	}

	public static Elem container(String name) {
		return Elem.create(name).setClosure(Closure.EXPLICIT);
	}
	
	public static Elem openElem(String name) {
		return Elem.create(name).setClosure(Closure.OPEN);
	}
	
	public static Elem h(int lev) {
		return container("h"+lev);
	}
	
	public static Elem div() {
		return container("div");
	}
	
	public static Elem div(Part content) {
		return container("div").set(content);
	}
	
	@Deprecated
	/** Usare Crete.div().setText
	 * 
	 * @param content
	 * @return
	 */
	public static Elem div(String content) {
		return container("div").set(content);
	}
	
	public static Elem table() {
		return container("table");
	}
	public static Elem tbody() {
		return container("tbody");
	}
	public static Elem thead() {
		return container("thead");
	}
	public static Elem tfoot() {
		return container("tfoot");
	}
	public static Elem tr() {
		return container("tr");
	}
	public static Elem tr(String[] tds) {
		Elem tr= container("tr");
		for (String s:tds)
			tr.add(Create.td( s));
		return tr;
	}
	public static Elem th() {
		return container("th");
	}
	public static Elem th(String content) {
		return container("th").set(content);
	}
	public static Elem td() {
		return container("td");
	}
	public static Elem td(String content) {
		return container("td").set(content);
	}
	public static Elem td(Part content) {
		return container("td").set(content);
	}
	public static Elem ul() {
		return container("ul");
	}
	public static Elem li() {
		return container("li");
	}
	
	public static Elem p() {
		return container("p");
	}
	
	public static Elem p(Part content) {
		return container("p").set(content);
	}
	
	public static Elem p(String content) {
		return container("p").set(content);
	}
	
	public static Elem span() {
		return container("span");
	}
	
	
	public static Elem a() {
		return container("a");
	}
	
	public static Elem b() {
		return container("b");
	}
	
	public static Elem i() {
		return container("i");
	}
	
	public static Elem sup() {
		return container("sup");
	}
	
	public static Elem script() {
		return container("script");
	}
	
	/**
	 * Restituisce un elemento {code}a{code} html che punta al link specificato.
	 * ATTENZIONE NON USARE perché il content viene inserito "as is" e non viene escappato.
	 * @param href  url non escappato
	 * @param content  contenuto del link
	 * @return  elemento {code}a{code} che punta al link specificato
	 */
	@Deprecated
	public static Elem a(String href, String content) {
		ElemHtml a = container("a").elemHtml();
		a.setHref(href);
		a.set(content);
		return a.elem();
	}
	
	public static Elem a(String href, Part content) {
		ElemHtml a = container("a").set(content).elemHtml();
		a.setHref(href);
		return a.elem();
	}
	
	public static Elem a(String href) {
		ElemHtml a = container("a").elemHtml();
		a.setHref(href);
		return a.elem();
	}
	
	public static Elem style(){
		return container("style");
	}
	
	private static Elem compact(String name) {
		return Elem.create(name).setClosure(Closure.COMPACT);
	}
	
	public static Elem br() {
		return compact("br");
	}
	
	public static Text nl() {
		return Text.creaAsIs("\n");
	}
	
	public static Text nbsp() {
		return Text.creaAsIs("&nbsp;");
	}
	
	public static Elem img() {
		return compact("img");
	}
	
	public static Elem img(String src, String alt) {
		return img().setAttr("src", src).setAttr("alt", alt);
	}
	
	public static Elem pre() {
		return container("pre");
	}
	
	private static Attr attr(String name,String value) {
		return Attr.create(name).setValue(value);
	}

	public static Attr href(String link) {
		return attr(Attr.href,link);
	}
	
	public static Attr src(String link) {
		return attr(Attr.src,link);
	}
	
	public static Attr id(String id) {
		return Attr.createId(id);
	}
	
	public static Attr clazz(String value) {
		return Attr.createClass(value);
	}
	
	public static Attr style(String value) {
		return Attr.createStyle(value);
	}
	
	/*
	 * FORM
	 */
	
	public static Elem form() {
		return container("form");
	}
	
	public static Elem formPost(String action) {
		return form().add(Attr.create("method","post")).add(Attr.create("action",action));
	}
	
	public static Elem label() {
		return container("label");
	}
	
	public static Elem label(String forId) {
		return container("label").addAttr("for", forId);
	}
	
	public static Elem select() {
		return container("select");
	}
	
	public static Elem select(String name) {
		return select().addAttr("name", name);
	}
	
	public static Attr selected() {
		return Attr.create("SELECTED");
	}

	
	public static Elem option() {
		return container("option");
	}
	
	public static Elem option(String value,String content) {
		return option().add(Attr.create("value").setValue(value)).set(content);
	}

	public static Elem input() {
		return openElem("input");
	}
	
	public static Elem input(String type,String name,String value) {
		return input().setAttr("type", type).addAttr("name", name).setAttr("value", value);
	}
	
	public static Elem textarea() {
		return container("textarea");
	}
	
	public static Elem textarea(String cols, String rows) {
		return textarea().setAttr("rows", rows).addAttr("cols", cols);
	}
	
	public static Elem inputSubmit(String name,String value) {
		return input("submit",name,value);
	}
	
	public static Elem button() {
		return container("button");
	}
	
	public static Elem meta() {
		return openElem("meta");
	}
	
	/*
	 * Tag HTML5 per markup semantico
	 */
	public static Elem main() {
		return container("main");
	}
	
	public static Elem aside() {
		return container("aside");
	}
	
	public static Elem header() {
		return container("header");
	}
	
	public static Elem footer() {
		return container("footer");
	}
	
	public static Elem nav() {
		return container("nav");
	}
	
	public static Elem section() {
		return container("section");
	}
	
	public static Elem article() {
		return container("article");
	}
}
