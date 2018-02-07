package meinoxxo;

import meinoxxo.model.Field;

public abstract class LogicForBothRowAndCol {

	public Field[][] apply(Field[][] board) {
		Field[][] ret = new Field[board.length][];

		// Rows
		for (int rowIdx=0; rowIdx<board.length; rowIdx++) {
			Field[] row = board[rowIdx];
			ret[rowIdx] = getLineOperation(row);
		}

		// Columns
		Field[][] switchedBoard = BoardBuilder.switchRowsAndColumns(ret);
		Field[][] ret2 = new Field[switchedBoard.length][];
		for (int rowIdx=0; rowIdx<switchedBoard.length; rowIdx++) {
			Field[] row = switchedBoard[rowIdx];
			ret2[rowIdx] = getLineOperation(row);
		}

		ret = BoardBuilder.switchRowsAndColumns(ret2);

		return ret;
	}

	public abstract Field[] getLineOperation(Field[] line);
}
