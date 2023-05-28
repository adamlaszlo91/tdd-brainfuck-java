package hu.evehcilabs.brainfuck.interpreter;

public class BrainfuckException extends RuntimeException {

	public BrainfuckException(String reason) {
		super(reason);
	}

	private static final long serialVersionUID = 1L;
}
