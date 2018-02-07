package meinoxxo;

import meinoxxo.model.Field;

public class Main {

	public static void main(String[] args) {
		try {
			Field[][] board = BoardBuilder.loadBoard("src/test/resources/games/03.txt");
			System.out.println("INPUT");
			Printer.print(board);
			Solver s = new Solver();
			Field[][] solvedBoard = s.solve(board);
			System.out.println("OUTPUT");
			Printer.print(solvedBoard);

			System.out.println("Iterations: " + s.getIterations());
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}

}
