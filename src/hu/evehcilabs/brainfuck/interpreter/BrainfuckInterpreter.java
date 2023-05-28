package hu.evehcilabs.brainfuck.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BrainfuckInterpreter {

	public static final int TAPE_SIZE = 30_000;

	private static final int INSTRUCTION_INCREASE_DATA_POINTER = '>';
	private static final int INSTRUCTION_DECREASE_DATA_POINTER = '<';
	private static final int INSTRUCTION_INCREASE_DATA = '+';
	private static final int INSTRUCTION_DECREASE_DATA = '-';
	private static final int INSTRUCTION_OUTPUT_DATA = '.';
	private static final int INSTRUCTION_INPUT_DATA = ',';
	private static final int INSTRUCTION_JUMP_IF_ZERO = '[';
	private static final int INSTRUCTION_JUMP_BACK_IF_NOT_ZERO = ']';

	private UnsignedByte[] tape;
	private int dataPointer;
	private int instructionPointer;
	private OutputStream outputStream;
	private InputStream inputStream;
	private byte[] instructions;

	public BrainfuckInterpreter() {
		prepareTape();
	}

	private void prepareTape() {
		tape = new UnsignedByte[TAPE_SIZE];
		for (int i = 0; i < TAPE_SIZE; i++) {
			tape[i] = new UnsignedByte();
		}
	}

	public void setOutputStream(OutputStream outpuStream) {
		this.outputStream = outpuStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public int getDataPointer() {
		return dataPointer;
	}

	public void execute(String string) {
		// Using bytes in here is fine, since all the control characters are < 128
		instructions = string.getBytes(StandardCharsets.US_ASCII);
		instructionPointer = 0;
		while (instructionPointer < instructions.length) {
			byte instruction = instructions[instructionPointer];
			if (INSTRUCTION_INCREASE_DATA_POINTER == instruction) {
				dataPointer++;
				if (dataPointer >= TAPE_SIZE) {
					throw new BrainfuckException(
							String.format("Data pointer has exceeded tape size (%d bytes)!", TAPE_SIZE));
				}
			}
			if (INSTRUCTION_DECREASE_DATA_POINTER == instruction) {
				dataPointer--;
				if (dataPointer < 0) {
					throw new BrainfuckException("Data pointer is below 0!");
				}
			}
			if (INSTRUCTION_INCREASE_DATA == instruction) {
				tape[dataPointer].increase();
			}
			if (INSTRUCTION_DECREASE_DATA == instruction) {
				tape[dataPointer].decrease();
			}
			if (INSTRUCTION_OUTPUT_DATA == instruction) {
				try {
					outputStream.write(tape[dataPointer].getValue());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (INSTRUCTION_INPUT_DATA == instruction) {
				try {
					tape[dataPointer].setValue(inputStream.read());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (INSTRUCTION_JUMP_IF_ZERO == instruction) {
				if (getData().getValue() == 0) {
					instructionPointer = findNextMatchingParenthesisInstructionPointer();
				} else {
					// Let the instruction pointer increase
				}
			}
			if (INSTRUCTION_JUMP_BACK_IF_NOT_ZERO == instruction) {
				if (getData().getValue() == 0) {
					// Let the instruction pointer increase
				} else {
					instructionPointer = findPreviousMatchingParenthesisInstructionPointer();
				}
			}
			instructionPointer++;
		}
	}

	private int findNextMatchingParenthesisInstructionPointer() {
		int nextInstructionPointer = instructionPointer;
		int depthCounter = 0;
		do {
			if (instructions[nextInstructionPointer] == INSTRUCTION_JUMP_IF_ZERO) {
				depthCounter++;
			} else if (instructions[nextInstructionPointer] == INSTRUCTION_JUMP_BACK_IF_NOT_ZERO) {
				depthCounter--;
				if (depthCounter == 0) {
					return nextInstructionPointer;
				}
			}
			nextInstructionPointer++;
		} while (nextInstructionPointer < instructions.length);
		throw new BrainfuckException(
				String.format("Could not find matching parenthesis for index: %d", instructionPointer));
	}

	private int findPreviousMatchingParenthesisInstructionPointer() {
		int previousInstructionPointer = instructionPointer;
		int depthCounter = 0;
		do {
			if (instructions[previousInstructionPointer] == INSTRUCTION_JUMP_BACK_IF_NOT_ZERO) {
				depthCounter++;
			} else if (instructions[previousInstructionPointer] == INSTRUCTION_JUMP_IF_ZERO) {
				depthCounter--;
				if (depthCounter == 0) {
					return previousInstructionPointer;
				}
			}
			previousInstructionPointer--;
		} while (previousInstructionPointer >= 0);
		throw new BrainfuckException(
				String.format("Could not find matching parenthesis for index: %d", instructionPointer));
	}

	public UnsignedByte getData() {
		return tape[dataPointer];
	}
}
