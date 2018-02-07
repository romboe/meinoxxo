package meinoxxo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import meinoxxo.model.Field;
import meinoxxo.model.Symbol;

public class BoardBuilder {



	public static Field[][] loadBoard(String filename) throws IOException {
		String[] strBoard = loadBoardAsArrayOfStrings(filename);
		Field[][] board = new Field[strBoard.length][];
		for (int row=0; row<strBoard.length; row++) {
			String strRow = strBoard[row];
			board[row] = buildFieldRow(strRow);
		}
		return board;
	}

	public static Field[][] buildBoardFromArrayOfStrings(String[] rows) {
		Field[][] board = new Field[rows.length][];
		for (int row=0; row<rows.length; row++) {
			String strRowLowerCase = rows[row].toLowerCase();
			board[row] = new Field[strRowLowerCase.length()];
			for (int col=0; col<strRowLowerCase.length(); col++) {
				char c = strRowLowerCase.charAt(col);
				Symbol s = getSymbolFromChar(c);
				board[row][col] = new Field(s);
			}
		}
		return board;
	}

	public static Field[] buildFieldRow(String strRow) {
		Field[] fieldRow = new Field[strRow.length()];
		String strRowLowerCase = strRow.toLowerCase();
		for (int col=0; col<strRow.length(); col++) {
			char c = strRowLowerCase.charAt(col);
			Symbol s = getSymbolFromChar(c);
			fieldRow[col] = new Field(s);
		}
		return fieldRow;
	}

	private static Symbol getSymbolFromChar(char c) {
		Symbol s = Symbol.EMPTY;
		switch(c) {
			case 'o': s = Symbol.O; break;
			case 'x': s = Symbol.X;
		}
		return s;
	}

	private static String[][] loadBoardAsStringArray(String filename) throws IOException {
		int[] dims = getDimensions(filename);
		String[][] board = new String[dims[0]][dims[1]];

		File file = new File(filename);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int row = 0;
			while ((line = br.readLine()) != null) {
				String[] cs = line.split("");
				for (int col=0; col<cs.length; col++) {
					board[row][col] = cs[col];
				}
				// System.out.println(line);
				row++;
			}

		}
		return board;
	}

	private static String[] loadBoardAsArrayOfStrings(String filename) throws IOException {
		int[] dims = getDimensions(filename);
		String[] board = new String[dims[0]];

		File file = new File(filename);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int row = 0;
			while ((line = br.readLine()) != null) {
				board[row++] = line;
			}

		}
		return board;
	}

	/**
	 * Returns the fields size in rows and cols.
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static int[] getDimensions(String filename) throws IOException {
		int rows = 0;
		int cols = -1;
		File file = new File(filename);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				rows++;
				cols = (-1 == cols) ? line.split("").length : cols;
			}
		}
		return new int[] {rows,cols};
	}

	public static List<Field[]> getColumns(Field[][] board) {
		List<Field[]> ret = new ArrayList<>();
		int cols = board[0].length; // first row determines number of cols
		for (int colIdx=0; colIdx<cols; colIdx++) {
			List<Field> columnValues = new ArrayList<>();
			for (int rowIdx=0; rowIdx<board.length; rowIdx++) {
				columnValues.add(board[rowIdx][colIdx]);
			}
			ret.add((Field[])columnValues.toArray(new Field[0]));
		}
		return ret;
	}

	/*
	 * 12
	 * 34
	 * 56
	 *
	 * 135
	 * 246
	 *
	 */
	public static Field[][] switchRowsAndColumns(Field[][] board) {
		int cols = board[0].length; // first row determines number of cols
		Field[][] ret = new Field[cols][board.length];
		for (int rowIdx=0; rowIdx<board.length; rowIdx++) {
			for (int colIdx=0; colIdx<cols; colIdx++) {
				ret[colIdx][rowIdx] = board[rowIdx][colIdx];
			}
		}
		return ret;
	}
}
