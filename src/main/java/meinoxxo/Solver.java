package meinoxxo;

import meinoxxo.model.Field;

public class Solver {

	private boolean debug = true;
	private int iterations;

	public Field[][] solve(Field[][] board) {
		Brain brain = new Brain();
		Field[][] ret = board;
		iterations = 0;
		do {
			brain.resetChanges();

			int old = brain.getChanges();
			ret = brain.fenceDoubles(ret);
			int neu = brain.getChanges();
			log("Fence Doubles", ret, neu-old);

			old = brain.getChanges();
			ret = brain.fillGaps(ret);
			neu = brain.getChanges();
			log("Fill Gaps", ret, neu-old);

			old = brain.getChanges();
			ret = brain.setTheOppositeOfTheLastMissingSymbol(ret);
			neu = brain.getChanges();
			log("Set the Opposite", ret, neu-old);

			old = brain.getChanges();
			ret = brain.completeLines(ret);
			neu = brain.getChanges();
			log("Complete line", ret, neu-old);

			old = brain.getChanges();
			ret = brain.compareRowsAndColumns(ret);
			neu = brain.getChanges();
			log("Compare rows and cols", ret, neu-old);

			iterations++;
		} while(brain.getChanges() > 0);

		return ret;
	}

	public int getIterations() {
		return iterations;
	}

	private void log(String msg, Field[][] board, int changes) {
		if (debug) {
			System.out.println(msg + " brought " + changes + " changes");
			Printer.print(board);
			System.out.println();
		}
	}
}
