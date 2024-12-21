package dev.upcraft.test.sparkweave.gametest;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class AlwaysTrueGameTest implements FabricGameTest {

    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void alwaysTrue(GameTestHelper ctx) {
        ctx.succeed();
    }
}
