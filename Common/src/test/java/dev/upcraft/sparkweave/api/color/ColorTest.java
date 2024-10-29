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
	}

}
