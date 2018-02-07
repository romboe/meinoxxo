package meinoxxo;

import java.util.ArrayList;
import java.util.List;

import meinoxxo.model.Field;
import meinoxxo.model.Symbol;

public class Brain {

	private int changes;
	private LogicForBothRowAndCol fenceDoublesLogic;
	private LogicForBothRowAndCol fillGapsLogic;
	private LogicForBothRowAndCol setTheOppositeOfTheLastMissingSymbolLogic;
	private LogicForBothRowAndCol completeLineLogic;


	public Brain() {
		this.fenceDoublesLogic = new LogicForBothRowAndCol() {
			@Override
			public Field[] getLineOperation(Field[] line) {
				return fenceDoublesInALine(line);
			}
		};
		this.fillGapsLogic = new LogicForBothRowAndCol() {
			@Override
			public Field[] getLineOperation(Field[] line) {
				return fillGapsInALine(line);
			}
		};
		this.setTheOppositeOfTheLastMissingSymbolLogic = new LogicForBothRowAndCol() {
			@Override
			public Field[] getLineOperation(Field[] line) {
				return setTheOppositeOfTheLastMissingSymbolInALine(line);
			}
		};
		this.completeLineLogic = new LogicForBothRowAndCol() {
			@Override
			public Field[] getLineOperation(Field[] line) {
				return completeLine(line);
			}
		};
	}

	public Field[][] fenceDoubles(Field[][] board) {
		return fenceDoublesLogic.apply(board);
	}

	public Field[][] fillGaps(Field[][] board) {
		return fillGapsLogic.apply(board);
	}

	public Field[][] setTheOppositeOfTheLastMissingSymbol(Field[][] board) {
		return setTheOppositeOfTheLastMissingSymbolLogic.apply(board);
	}

	public Field[][] completeLines(Field[][] board) {
		return completeLineLogic.apply(board);
	}

	public Field[][] compareRowsAndColumns(Field[][] board) {
		Field[][] ret = new Field[board.length][];

		// Rows
		for (int rowIdx=0; rowIdx<board.length; rowIdx++) {
			Field[] row = board[rowIdx];
			findIdenticalCompleteLineAndFillUp(board, row, rowIdx, ret);
		}

		// Columns
		Field[][] switchedBoard = BoardBuilder.switchRowsAndColumns(ret);
		Field[][] ret2 = new Field[switchedBoard.length][];
		for (int rowIdx=0; rowIdx<switchedBoard.length; rowIdx++) {
			Field[] row = switchedBoard[rowIdx];
			findIdenticalCompleteLineAndFillUp(board, row, rowIdx, ret2);
		}

		ret = BoardBuilder.switchRowsAndColumns(ret2);

		return ret;
	}

	public void findIdenticalCompleteLineAndFillUp(final Field[][] board, Field[] line, int lineIdx, Field[][] ret) {
		boolean lineSetFlag = false;
		if (2 == countEmptyFieldsInALine(line)) {
			Field[] identicalCompleteLine = findIdenticalCompleteLine(board, line);
			if (null != identicalCompleteLine) {
				ret[lineIdx] = fillUpLineWithOppositeSymbols(line, identicalCompleteLine);
				lineSetFlag = true;
			}
		}
		if (!lineSetFlag) {
			ret[lineIdx] = line;
		}
	}

	public Field[] findIdenticalCompleteLine(Field[][] board, Field[] line) {
		Field[] ret = null;
		for (int i=0; i<board.length; i++) {
			Field[] lineFromBoard = board[i];
			if (0 == countEmptyFieldsInALine(lineFromBoard) && areLinesIdentical(lineFromBoard, line)) {
				ret = lineFromBoard;
				break;
			}
		}
		return ret;
	}

	/**
	 * Except for blanks all symbols have to be same.
	 * @param line1
	 * @param line2
	 * @return
	 */
	public boolean areLinesIdentical(Field[] line1, Field[] line2) {
		if (line1.length != line2.length) {
			return false;
		}
		for (int i=0; i<line1.length; i++) {
			Field f1 = line1[i];
			Field f2 = line2[i];
			boolean ident = f1.isEmpty() || f2.isEmpty() || f1.getSymbol() == f2.getSymbol();
			if (!ident) {
				return false;
			}
		}
		return true;
	}

	public Field[] fillUpLineWithOppositeSymbols(Field[] incompleteLine, Field[] completeLine) {
		Field[] ret = clone(incompleteLine);
		for (int i=0; i<completeLine.length; i++) {
			Field f = ret[i];
			if (f.isEmpty()) {
				setSymbol(f, Symbol.getOpposite(completeLine[i].getSymbol()));
			}
		}
		return ret;
	}

	public List<Field[]> findLinesWithTwoEmptyFields(Field[][] board) {
		List<Field[]> ret = new ArrayList<>();
		for (int i=0; i<board.length; i++) {
			Field[] line = board[i];
			if (2 == countEmptyFieldsInALine(line)) {
				ret.add(line);
			}
		}
		return ret;
	}

	public Field[] completeLine(final Field[] line) {
		Field[] ret = clone(line);
		int max = getMaxSymbolCountPerLine(line);
		int xCounter = countSymbolsInALine(line, Symbol.X);
		int oCounter = countSymbolsInALine(line, Symbol.O);
		if  ((xCounter == max) && (oCounter < max)) {
			fillUpLineWithSymbol(ret, Symbol.O);
		}
		else if ((oCounter == max) && (xCounter < max)) {
			fillUpLineWithSymbol(ret, Symbol.X);
		}
		return ret;
	}

	private Field[] fillUpLineWithSymbol(Field[] line, Symbol s) {
		Field[] ret = line;
		for (Field f:ret) {
			setSymbol(f, s);
		}
		return ret;
	}

	public Field[] fenceDoublesInALine(final Field[] line) {
		Field[] ret = clone(line);

		int xCount = 0;
		int oCount = 0;
		for (int i=0; i<line.length; i++) {
			Field field = line[i];

			if (field.isEmpty()) {
				xCount = 0;
				oCount = 0;
			}
			else if (field.isX()) {
				oCount=0;
				xCount++;
			}
			else if (field.isO()) {
				xCount=0;
				oCount++;
			}
			if (2 == xCount) {
				fence(ret, i-2, i+1, Symbol.O);
			}
			else if (2 == oCount) {
				fence(ret, i-2, i+1, Symbol.X);
			}
		}
		return ret;
	}

	public Field[] fillGapsInALine(final Field[] line) {
		Field[] ret = clone(line);
		for (int i=0; i<line.length; i++) {
			Field field = line[i];
			if (field.isEmpty()) {
				Symbol symbolLeft = null;
				if (i-1 >= 0) {
					symbolLeft = line[i-1].getSymbol();
				}
				Symbol symbolRight = null;
				if (i+1 < line.length) {
					symbolRight = line[i+1].getSymbol();
				}
				if (symbolLeft == symbolRight && symbolLeft != Symbol.EMPTY) {
					ret[i].setSymbol(Symbol.getOpposite(symbolLeft));
					changes++;
				}
			}
		}
		return ret;
	}

	/*
	 * Doesn't test consistency.
	 */
	public boolean areThreeOrMorePossible(final Field[] line) {
		// Empty means interested generally
		return this.areThreeOrMorePossible(line, Symbol.EMPTY);
	}

	public boolean areThreeOrMorePossible(final Field[] line, Symbol symbolWeAreInterestedIn) {
		for (int i=0; i<line.length; i++) {
			Field field = line[i];
			// for each symbol count in both directions
			Symbol s = field.getSymbol();
			if (symbolWeAreInterestedIn != Symbol.EMPTY) {
				if (s != Symbol.EMPTY && s != symbolWeAreInterestedIn) {
					continue;
				}
			}
			int j=i;
			int counter = 1; // 1 for the symbol we've got

			// look behind
			while(j-- > 0) {
				// Same symbol or empty
				if ((line[j].getSymbol() == s) || line[j].isEmpty()) {
					counter++;
				}
				else {
					break;
				}
			}
			if (counter > 2) {
				return true;
			}

			// look ahead
			j=i;
			while(++j < line.length) {
				// Same symbol or empty
				if ((line[j].getSymbol() == s) || line[j].isEmpty()) {
					counter++;
				}
				else {
					break;
				}
			}
			if (counter > 2) {
				return true;
			}
		}
		return false;
	}

	public static  int getMaxSymbolCountPerLine(Field[] line) {
		return line.length / 2;
	}

	public boolean isConsistent(Field[] line) {
		if (line.length < 4) {
			return false;
		}
		if (line.length % 2 != 0) {
			return false;
		}
		int maxSymbolCount = getMaxSymbolCountPerLine(line);
		int xCounter = 0;
		int xConsecutiveCounter = 0;
		int oCounter = 0;
		int oConsecutiveCounter = 0;

		for (Field f:line) {
			if (f.isO()) {
				oCounter++;
				oConsecutiveCounter++;
				xConsecutiveCounter = 0;
			}
			else if (f.isX()) {
				xCounter++;
				xConsecutiveCounter++;
				oConsecutiveCounter = 0;
			}
			else if (f.isEmpty()) {
				xConsecutiveCounter = 0;
				oConsecutiveCounter = 0;
			}

			if ((xCounter > maxSymbolCount) || (oCounter > maxSymbolCount)) {
				return false;
			}
			if ((xConsecutiveCounter > 2) || (oConsecutiveCounter > 2)) {
				return false;
			}
		}
		return true;
	}

	public Field[] setTheOppositeOfTheLastMissingSymbolInALine(final Field[] line) {
		return setTheOppositeOfTheLastMissingSymbolInALine(setTheOppositeOfTheLastMissingSymbolInALine(line, Symbol.X), Symbol.O);
	}

	public Field[] setTheOppositeOfTheLastMissingSymbolInALine(final Field[] line, Symbol s) {
		Field[] ret = clone(line);
		int lineLength = ret.length;
		int symbolCount = countSymbolsInALine(line, s);
		if (symbolCount == lineLength / 2 - 1) { // just one is missing
			int emptyFieldCount = countEmptyFieldsInALine(ret);
			int skipNTimes = 0;
			for (int i=0; i<emptyFieldCount; i++) {
				Field[] backup = clone(ret);
				ret = setSymbolIntoNextEmptyField(ret, s, skipNTimes);
				if (isConsistent(ret) && areThreeOrMorePossible(ret, Symbol.getOpposite(s))) {
					ret = backup;
					this.changes--;
					ret = setSymbolIntoNextEmptyField(ret, Symbol.getOpposite(s), skipNTimes);
					skipNTimes = 0;
				}
				else {
					ret = backup;
					this.changes--;
					skipNTimes++;
				}
			}
		}
		return ret;
	}

	public int countEmptyFieldsInALine(Field[] line) {
		return countSymbolsInALine(line, Symbol.EMPTY);
	}

	public int countSymbolsInALine(Field[] line, Symbol s) {
		int counter = 0;
		for (Field f:line) {
			if (f.getSymbol() == s) {
				counter++;
			}
		}
		return counter;
	}

	public Field[] setSymbolIntoNextEmptyField(Field[] line, Symbol s) {
		return this.setSymbolIntoNextEmptyField(line, s, 0);
	}

	public Field[] setSymbolIntoNextEmptyField(Field[] line, Symbol s, int skipNTimes) {
		Field[] ret = clone(line);
		for (Field f:ret) {
			if (f.isEmpty()) {
				if (0 == skipNTimes--) {
					setSymbol(f, s);
					break;
				}
			}
		}
		return ret;
	}

	public int getChanges() {
		return this.changes;
	}

	public void resetChanges() {
		this.changes = 0;
	}

	private void fence(Field[] row, int begin, int end, Symbol s) {
		if (begin >= 0) {
			setSymbol(row, begin, s);
		}
		if (end < row.length) {
			setSymbol(row, end, s);
		}
	}

	private void setSymbol(Field[] line, int i, Symbol s) {
		setSymbol(line[i], s);
	}

	private void setSymbol(Field f, Symbol s) {
		if (f.isEmpty()) {
			if (Symbol.X == s) {
				f.setX();
				changes++;
			}
			else if (Symbol.O == s) {
				f.setO();
				changes++;
			}
		}
	}

	private static Field[] clone(Field[] original) {
		Field[] ret = new Field[original.length];
		for (int i=0; i<ret.length; i++) {
			ret[i] = original[i].clone();
		}
		return ret;
	}
}
