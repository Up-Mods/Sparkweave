package dev.upcraft.sparkweave.api.color;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorTest {

	@BeforeAll
	public static void setup() {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
	}

	@Test
	public void testRGB() {
		var color = Color.fromRGB(255, 50, 137);
		assertEquals(255, color.alpha());
		assertEquals(255, color.red());
		assertEquals(50, color.green());
		assertEquals(137, color.blue());

		color = Color.fromInt(0x123456, Color.Ordering.RGB);
		assertEquals(0x12, color.red());
		assertEquals(0x34, color.green());
		assertEquals(0x56, color.blue());
		assertEquals(0xFF, color.alpha());

		assertEquals("123456", Integer.toHexString(color.asInt(Color.Ordering.RGB)));
	}

	@Test
	public void testARGB() {
		var color = Color.fromARGB(0x12345678);
		assertEquals(0x12, color.alpha());
		assertEquals(0x34, color.red());
		assertEquals(0x56, color.green());
		assertEquals(0x78, color.blue());

		assertEquals("12345678", Integer.toHexString(color.asIntARGB()));
	}

	@Test
	public void testARGBtoBGR() {
		var color = Color.fromARGB(0x12345678);

		assertEquals(0x12, color.alpha());
		assertEquals(0x34, color.red());
		assertEquals(0x56, color.green());
		assertEquals(0x78, color.blue());

		assertEquals("785634", Integer.toHexString(color.asInt(Color.Ordering.BGR)));
	}
}
