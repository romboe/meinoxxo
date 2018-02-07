package meinoxxo.model;

public enum Symbol {
	X, O, EMPTY;

	public static Symbol getOpposite(Symbol s) {
		switch(s) {
		case X: return Symbol.O;
		case O: return Symbol.X;
		}
		return Symbol.EMPTY;
	}
}
