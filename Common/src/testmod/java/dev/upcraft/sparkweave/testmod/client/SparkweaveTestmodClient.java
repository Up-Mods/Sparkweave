package dev.upcraft.sparkweave.testmod.client;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.api.client.Debug;
import dev.upcraft.sparkweave.api.client.event.ClientTickEvents;
import dev.upcraft.sparkweave.api.client.event.RegisterCustomArmorRenderersEvent;
import dev.upcraft.sparkweave.api.client.event.RegisterLayerDefinitionsEvent;
import dev.upcraft.sparkweave.api.client.event.RegisterLecternItemRendererEvent;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.testmod.client.models.MageRobesModel;
import dev.upcraft.sparkweave.testmod.client.renderers.DiamondLecternRenderer;
import dev.upcraft.sparkweave.testmod.client.renderers.MageRobesRenderer;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;

@AutoService(ClientEntryPoint.class)
public class SparkweaveTestmodClient implements ClientEntryPoint {

	private static int ticks = 0;

	@Override
	public void onInitializeClient(ModContainer mod) {
		RegisterLayerDefinitionsEvent.EVENT.register(event -> event.registerModelLayers(MageRobesModel.MODEL_LAYER, MageRobesModel::createBodyLayer));
		RegisterCustomArmorRenderersEvent.EVENT.register(event -> event.register(MageRobesRenderer::new, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS));
		RegisterCustomArmorRenderersEvent.EVENT.register(event -> event.register(MageRobesRenderer::new, TestItems.MAGE_HOOD, TestItems.MAGE_ROBES, TestItems.MAGE_LEGGINGS, TestItems.MAGE_BOOTS));
		RegisterLecternItemRendererEvent.EVENT.register(event -> event.registerRenderer(DiamondLecternRenderer::new, TestItems.TEST_ITEM));
		ClientTickEvents.START_TICK.register(SparkweaveTestmodClient::onClientTickStart);
	}

	public static void onClientTickStart(Minecraft client) {
		var bp = new BlockPos.MutableBlockPos();
		if (client.level != null && ticks++ % 100 == 0) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Debug.setColor(50 + x * 10, 0, 135 + z * 8);
					bp.set(x, 0, z);
					var height = client.level.getHeight(Heightmap.Types.WORLD_SURFACE, bp);
					Debug.drawLine(x + 0.5F, height, z + 0.5F, x + 0.5F, height + 1, z + 0.5F, 5000); // TODO time format
				}
			}
		}
	}
}
