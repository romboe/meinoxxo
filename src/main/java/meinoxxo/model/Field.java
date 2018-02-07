package meinoxxo.model;

public class Field {

	private Symbol symbol;


	public Field(Symbol symbol) {
		this.symbol = symbol;
	}

	public boolean isX() {
		return Symbol.X.equals(symbol);
	}

	public void setX() {
		symbol = Symbol.X;
	}

	public boolean isO() {
		return Symbol.O.equals(symbol);
	}

	public void setO() {
		symbol = Symbol.O;
	}

	public boolean isEmpty() {
		return Symbol.EMPTY.equals(symbol);
	}

	public Field clone() {
		return new Field(this.symbol);
	}

	public void clear() {
		this.symbol = Symbol.EMPTY;
	}

	public Symbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public String printSymbol() {
		String ret = null;
		switch(this.symbol) {
		case O: ret = "O"; break;
		case X: ret = "X"; break;
		case EMPTY: ret = "-";
		}
		return ret;
	}

	@Override
	public String toString() {
		return printSymbol();
	}
}
