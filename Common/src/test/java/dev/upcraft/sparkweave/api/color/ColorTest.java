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
		assertEquals(color.alpha(), 255);
		assertEquals(color.red(), 255);
		assertEquals(color.green(), 50);
		assertEquals(color.blue(), 137);
	}

}
