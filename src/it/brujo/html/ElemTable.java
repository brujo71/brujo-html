package it.brujo.html;

public class ElemTable {

	Elem table;
	public ElemTable() {
		this(Create.table());
	}
	public ElemTable(Elem table) {
		this.table = table;
		if (!table.nameLC().equals("table"))
			throw new RuntimeException("not valid elem "+table.name());
	}

	public ElemTable addRow(String... tds) {
		Elem tr=Create.tr();
		table.add(tr);
		for (String td:tds)
			tr.add(Create.td().setText(td));
		return this;
	}
	
	public ElemTable addRowObj(Object... tds) {
		Elem tr=Create.tr().addTo(table);
		for (Object td:tds)
			tr.add(Create.td().setText(td==null ? "-" : td.toString()));
		return this;
	}
	
	public <P extends Part> P addTo(MultiPart e) {
		return table.addTo(e);
	}
	
	public Elem table() {
		return table;
	}
	
}
