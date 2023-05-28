package hu.evehcilabs.brainfuck.interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnsignedByteTest {

	@Test
	void testDefaultValue() {
		UnsignedByte uByte = new UnsignedByte();
		assertEquals(0, uByte.getValue());
	}

	@Test
	void testIncreaseValue() {
		UnsignedByte uByte = new UnsignedByte();
		uByte.increase();
		assertEquals(1, uByte.getValue());
		uByte.increase();
		uByte.increase();
		uByte.increase();
		assertEquals(4, uByte.getValue());
	}

	@Test
	void testDecreaseValue() {
		UnsignedByte uByte = new UnsignedByte();
		uByte.increase();
		uByte.increase();
		uByte.decrease();
		assertEquals(1, uByte.getValue());
		uByte.decrease();
		assertEquals(0, uByte.getValue());
	}

	@Test
	void testValueOverFlow() {
		UnsignedByte uByte = new UnsignedByte();
		for (int i = 1; i <= 256; i++) {
			uByte.increase();
		}
		assertEquals(0, uByte.getValue());
	}

	@Test
	void testValueUnderFlow() {
		UnsignedByte uByte = new UnsignedByte();
		uByte.decrease();
		assertEquals(255, uByte.getValue());
	}

	@Test
	void testSetValue() {
		UnsignedByte uByte = new UnsignedByte();
		uByte.setValue(12);
		assertEquals(12, uByte.getValue());
	}

	@Test
	void testSetInvalidValue() {
		UnsignedByte uByte = new UnsignedByte();
		uByte.setValue(12);
		Assertions.assertThrows(BrainfuckException.class, () -> {
			uByte.setValue(-1);
		});
		Assertions.assertThrows(BrainfuckException.class, () -> {
			uByte.setValue(256);
		});
	}

}
