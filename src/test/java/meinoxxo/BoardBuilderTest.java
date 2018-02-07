package meinoxxo;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import meinoxxo.model.Field;

public class BoardBuilderTest {


	@Before
	public void before() {
	}

	@Test
	public void test() throws IOException {
		Printer.print(BoardBuilder.loadBoard("src/test/resources/games/01.txt"));
	}

	@Test
	public void testDimensions() throws IOException {
		int[] ret = BoardBuilder.getDimensions("src/test/resources/games/01.txt");
		assertEquals(10, ret[0]);
		assertEquals(8, ret[1]);
	}

	@Test
	public void testBuildFieldRow() {
		Field[] row = BoardBuilder.buildFieldRow("------");
		Printer.printLine(row);

		row = BoardBuilder.buildFieldRow("-x-x-x");
		Printer.printLine(row);
	}

	@Test
	public void testGetColumns() throws IOException {
		Field[][] board = BoardBuilder.loadBoard("src/test/resources/games/01.txt");
		List<Field[]> cols = BoardBuilder.getColumns(board);
		assertEquals("xx--------".toUpperCase(), Printer.printString(cols.get(0)));
	}

	@Test
	public void testSwitchRowsAndCols() throws IOException {
		Field[][] board = BoardBuilder.loadBoard("src/test/resources/games/01.txt");
		Field[][] switchedBoard = BoardBuilder.switchRowsAndColumns(board);
		System.out.println("Switched board");
		Printer.print(switchedBoard);
		assertEquals("xx--------".toUpperCase(), Printer.printString(switchedBoard[0]));
		assertEquals("------oo-o".toUpperCase(), Printer.printString(switchedBoard[1]));
	}

	@Test
	public void testLoadBoard() {
		String[] input = new String[]{"x--","-x-","--o"};
		Field[][] board = BoardBuilder.buildBoardFromArrayOfStrings(input);
		assertTrue(board[0][0].isX());
		assertTrue(board[1][1].isX());
		assertTrue(board[2][2].isO());
	}
}
