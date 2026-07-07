package dev.upcraft.sparkweave.testmod.client;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.api.client.Debug;
import dev.upcraft.sparkweave.api.client.event.ClientTickEvents;
import dev.upcraft.sparkweave.api.client.event.RegisterLayerDefinitionsEvent;
import dev.upcraft.sparkweave.api.client.event.RegisterLecternItemRendererEvent;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.testmod.client.models.MageRobesModel;
import dev.upcraft.sparkweave.testmod.client.renderers.DiamondLecternRenderer;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.client.Minecraft;

@AutoService(ClientEntryPoint.class)
public class SparkweaveTestmodClient implements ClientEntryPoint {

	private static int ticks = 0;

	@Override
	public void onInitializeClient(ModContainer mod) {
		RegisterLayerDefinitionsEvent.EVENT.register(event -> event.registerModelLayers(MageRobesModel.MODEL_LAYER, MageRobesModel::createBodyLayer));
		// TODO armor
//		RegisterCustomArmorRenderersEvent.EVENT.register(event -> event.register((_, context, _) -> new MageRobesRenderer(context), Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS));
		RegisterLecternItemRendererEvent.EVENT.register(event -> event.registerRenderer(DiamondLecternRenderer::new, TestItems.TEST_ITEM));
		ClientTickEvents.START_TICK.register(SparkweaveTestmodClient::onClientTickStart);
	}

	public static void onClientTickStart(Minecraft client) {
		if (client.level != null && ticks++ % 100 == 0) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Debug.setColor(50 + x * 10, 0, 135 + z * 8);
					Debug.drawLine(x, 0, z, x, 1, z, 100); // TODO time format
				}
			}
		}
	}
}
