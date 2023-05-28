package hu.evehcilabs.brainfuck.interpreter;

// Java's byte is signed, but Brainfuck assumes unsigned values to represent ASCII characters. This class is a wrapper to act like an unsigned byte.
public class UnsignedByte {

	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 255;

	private int value;

	public int getValue() {
		return value;
	}

	public void increase() {
		value++;
		if (value > MAX_VALUE) {
			value = MIN_VALUE;
		}
	}

	public void decrease() {
		value--;
		if (value < MIN_VALUE) {
			value = MAX_VALUE;
		}
	}

	public void setValue(int value) {
		if (value < MIN_VALUE) {
			throw new BrainfuckException(String.format("Signed byte value cannot be lower than %s", MIN_VALUE));
		}
		if (value > MAX_VALUE) {
			throw new BrainfuckException(String.format("Signed byte value cannot be higher than %s", MAX_VALUE));
		}
		this.value = value;
	}

}
