package meinoxxo;

import static meinoxxo.BoardBuilder.buildFieldRow;
import static meinoxxo.Printer.printString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import meinoxxo.model.Field;
import meinoxxo.model.Symbol;

public class BrainTest {

	private Brain brain;


	@Before
	public void setup() {
		this.brain = new Brain();
	}

	@Test
	public void testFenceDoublesInARow() {
		Field[] row = buildFieldRow("oo----");
		Field[] ret = brain.fenceDoublesInALine(row);
		assertEquals("OOX---", Printer.printString(ret));
		assertEquals(1, brain.getChanges());

		row = buildFieldRow("--oo--");
		ret = brain.fenceDoublesInALine(row);
		assertEquals("-XOOX-", printString(ret));

		row = buildFieldRow("----oo");
		ret = brain.fenceDoublesInALine(row);
		assertEquals("---XOO", printString(ret));

		row = buildFieldRow("xoo-");
		ret = brain.fenceDoublesInALine(row);
		assertEquals("XOOX", printString(ret));

		row = buildFieldRow("xxooxx");
		brain.resetChanges();
		ret = brain.fenceDoublesInALine(row);
		assertEquals("xxooxx".toUpperCase(), printString(ret));
		assertEquals(0, brain.getChanges());
	}

	@Test
	public void testFillGaps() {
		Field[] row = buildFieldRow("-x-x--x---x-x");
		Field[] ret = brain.fillGapsInALine(row);
		assertEquals("-XOX--X---XOX", printString(ret));
		assertEquals(2, brain.getChanges());
	}

	@Test
	public void testFenceDoubles() throws IOException {
		Field[][] board = BoardBuilder.loadBoard("src/test/resources/games/01.txt");
		Field[][] fencedBoard = brain.fenceDoubles(board);
		System.out.println("Fenced Board");
		Printer.print(fencedBoard);
	}

	@Test
	public void testAreThreeOrMorePossible() {
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("------")));
		assertTrue("wouldn't be consistent but it's possible", brain.areThreeOrMorePossible(buildFieldRow("x-xx--")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("xx----")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("x----x")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("---o----")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("x-x---")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("x--x--")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("xoo-xo")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("x--xoox---x")));
		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("xxox--")));

		assertFalse(brain.areThreeOrMorePossible(buildFieldRow("xoxoxoxo")));
		assertFalse(brain.areThreeOrMorePossible(buildFieldRow("x-oxo-xo")));

		assertTrue(brain.areThreeOrMorePossible(buildFieldRow("xx---x"), Symbol.O));
	}

	@Test
	public void testCountSymbolsInALine() {
		assertEquals(0, brain.countSymbolsInALine(buildFieldRow("----"), Symbol.X));
		assertEquals(5, brain.countSymbolsInALine(buildFieldRow("x-x-xx-oo-x"), Symbol.X));
		assertEquals(2, brain.countSymbolsInALine(buildFieldRow("x-x-xx-oo-x"), Symbol.O));
	}

	@Test
	public void testSetSymbolIntoNextEmptyField() {
		assertEquals("X--", printString(brain.setSymbolIntoNextEmptyField(buildFieldRow("---"), Symbol.X, 0)));
		assertEquals("-X-", printString(brain.setSymbolIntoNextEmptyField(buildFieldRow("---"), Symbol.X, 1)));
		assertEquals("--X", printString(brain.setSymbolIntoNextEmptyField(buildFieldRow("---"), Symbol.X, 2)));
		assertEquals("---", printString(brain.setSymbolIntoNextEmptyField(buildFieldRow("---"), Symbol.X, 3)));

		assertEquals("OXX", printString(brain.setSymbolIntoNextEmptyField(buildFieldRow("OX-"), Symbol.X, 0)));
	}

	@Test
	public void testSetTheOppositeOfTheLastMissingSymbolInALine() {
		assertEquals("XX---O", printString(brain.setTheOppositeOfTheLastMissingSymbolInALine(buildFieldRow("xx----"), Symbol.X)));
		assertEquals("XOOXOX---X", printString(brain.setTheOppositeOfTheLastMissingSymbolInALine(buildFieldRow("x--xox---x"), Symbol.X)));
		assertEquals("XOXO--OX", printString(brain.setTheOppositeOfTheLastMissingSymbolInALine(buildFieldRow("XOX----X"), Symbol.X)));
		assertEquals("O--XOXXO", printString(brain.setTheOppositeOfTheLastMissingSymbolInALine(buildFieldRow("o--xo-xo"), Symbol.O)));
		brain.resetChanges();
		brain.setTheOppositeOfTheLastMissingSymbolInALine(buildFieldRow("o--xo-xo"), Symbol.O);
		assertEquals(1, brain.getChanges());
	}

	@Test
	public void testIsConsistent() {
		assertTrue(brain.isConsistent(buildFieldRow("----")));
		assertFalse("not even", brain.isConsistent(buildFieldRow("-----")));
		assertFalse(brain.isConsistent(buildFieldRow("xxx---")));
		assertTrue(brain.isConsistent(buildFieldRow("xx-x----")));
		assertTrue(brain.isConsistent(buildFieldRow("xoxxox---x")));
	}

	@Test
	public void testCompleteLine() {
		assertEquals("XXOXOOXO", printString(brain.completeLine(buildFieldRow("XXOXO-XO"))));
		assertEquals("XXOOXOOX", printString(brain.completeLine(buildFieldRow("XXOOX--X"))));
	}

	@Test
	public void testFillUpLineWithOppositeSymbols() {
		assertEquals("XOXOOXOX",
			printString(brain.fillUpLineWithOppositeSymbols(buildFieldRow("--xooxox"), buildFieldRow("oxxooxox"))));
	}

	@Test
	public void testAreLinesIdentical() {
		assertTrue(brain.areLinesIdentical(buildFieldRow("oxxooxox"), buildFieldRow("--------")));
		assertTrue(brain.areLinesIdentical(buildFieldRow("oxxooxox"), buildFieldRow("o-x-oxox")));
	}

	@Test
	public void testFindIdenticalCompleteLineAndFillUp() {
		String[] input = new String[]{"--oxoxxo","---x-o--","xooxoxxo","--oxoxox"};
		Field[][] board = BoardBuilder.buildBoardFromArrayOfStrings(input);
		int lineIdx = 0;
		Field[][] ret = new Field[board.length][];
		brain.findIdenticalCompleteLineAndFillUp(board, board[lineIdx], lineIdx, ret);
		assertEquals("OXOXOXXO", printString(ret[lineIdx]));
	}
}
