package meinoxxo;

import meinoxxo.model.Field;

public class Printer {

	public static void print(String[][] board) {

		for (int row=0; row<board.length; row++) {
			for (int col=0; col<board[row].length; col++) {
				System.out.print(board[row][col]);
			}
			System.out.println();
		}
	}

	public static void print(Field[][] board) {
		for (int row=0; row<board.length; row++) {
			printLine(board[row]);
		}
	}

	public static void printLine(Field[] line) {
		for (int i=0; i<line.length; i++) {
			System.out.print(line[i].printSymbol());
		}
		System.out.println();
	}

	public static String printString(Field[] f) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<f.length; i++) {
			sb.append(f[i].printSymbol());
		}
		return sb.toString();
	}
}
