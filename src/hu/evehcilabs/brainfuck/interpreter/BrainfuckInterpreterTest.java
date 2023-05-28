package hu.evehcilabs.brainfuck.interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BrainfuckInterpreterTest {

	private class OutputStreamDummy extends OutputStream {
		public int[] writtenData = new int[100];
		int index;

		@Override
		public void write(int b) throws IOException {
			writtenData[index] = b;
			index++;
		}
	}

	private class InputStreamDummy extends InputStream {

		private int testValue;

		public InputStreamDummy(int testValue) {
			super();
			this.testValue = testValue;
		}

		@Override
		public int read() throws IOException {
			return testValue;
		}
	}

	@Test
	void testInitialState() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		assertEquals(0, interpreter.getDataPointer());
	}

	@Test
	void testIncreaseDataPointer() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		interpreter.execute(">");
		assertEquals(1, interpreter.getDataPointer());
		interpreter.execute(">>");
		assertEquals(3, interpreter.getDataPointer());
	}

	@Test
	void testDecreaseDataPointer() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		interpreter.execute(">>");
		interpreter.execute("<");
		assertEquals(1, interpreter.getDataPointer());
		interpreter.execute("<");
		assertEquals(0, interpreter.getDataPointer());
	}

	@Test
	void testDataPointerOverflow() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		Assertions.assertThrows(BrainfuckException.class, () -> {
			for (int i = 1; i <= BrainfuckInterpreter.TAPE_SIZE; i++) {
				interpreter.execute(">");
			}
		});
	}

	@Test
	void testDataPointerUnderflow() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		Assertions.assertThrows(BrainfuckException.class, () -> {
			interpreter.execute("<");
		});
	}

	@Test
	void testIncreaseData() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		assertEquals(0, interpreter.getData().getValue());
		interpreter.execute("+");
		assertEquals(1, interpreter.getData().getValue());
	}

	@Test
	void testDecreaseData() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		interpreter.execute("++");
		assertEquals(2, interpreter.getData().getValue());
		interpreter.execute("-");
		assertEquals(1, interpreter.getData().getValue());
	}

	@Test
	void testDataOverflow() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		for (int i = 1; i <= 256; i++) {
			interpreter.execute("+");
		}
		assertEquals(0, interpreter.getData().getValue());
	}

	@Test
	void testDataUnderflow() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		for (int i = 1; i <= 256; i++) {
			interpreter.execute("-");
		}
		assertEquals(0, interpreter.getData().getValue());
	}

	@Test
	void testOutput() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		OutputStreamDummy outpuStream = new OutputStreamDummy();
		interpreter.setOutputStream(outpuStream);
		interpreter.execute("+.");
		assertEquals(1, outpuStream.writtenData[0]);
	}

	@Test
	void testInput() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		InputStreamDummy inputStream = new InputStreamDummy(1);
		interpreter.setInputStream(inputStream);
		interpreter.execute(",");
		assertEquals(1, interpreter.getData().getValue());
	}

	@Test
	void testLoop() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		interpreter.execute("+++++[-]");
		assertEquals(0, interpreter.getData().getValue());
	}

	@Test
	void testPrintH() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		OutputStreamDummy outputStream = new OutputStreamDummy();
		interpreter.setOutputStream(outputStream);
		interpreter.execute(">+++++++++[<++++++++>-]<.");
		int[] expected = new int[] { 'H' };
		int[] result = Arrays.copyOfRange(outputStream.writtenData, 0, expected.length);
		assertTrue(Arrays.equals(expected, result));
	}

	// @Test
	void testPrintHelloWorld() {
		BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
		OutputStreamDummy outputStream = new OutputStreamDummy();
		interpreter.setOutputStream(outputStream);
		interpreter.execute(
				"++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.");
		int[] expected = new int[] { 'H', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd', '!' };
		int[] result = Arrays.copyOfRange(outputStream.writtenData, 0, expected.length);
		assertTrue(Arrays.equals(expected, result));
	}

}
