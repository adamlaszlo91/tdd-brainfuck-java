package hu.evehcilabs.brainfuck;

import hu.evehcilabs.brainfuck.interpreter.BrainfuckInterpreter;

public class Main {

	public static void main(String[] args) {
		BrainfuckInterpreter interpreter;

		interpreter = new BrainfuckInterpreter();
		interpreter.setInputStream(System.in);
		interpreter.setOutputStream(System.out);

		for (int i = 0; i < 256; i++) {
			interpreter.execute(".+");
		}

		interpreter.execute(">++++++++++.>");

		interpreter.execute(",.");

		// Output stream might need flush()
		System.out.flush();
	}

}
