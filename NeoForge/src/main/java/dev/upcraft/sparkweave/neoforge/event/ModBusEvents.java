package dev.upcraft.sparkweave.neoforge.event;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.event.RegisterCustomLecternMenuEvent;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = SparkweaveMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

	@SubscribeEvent
	public static void processBlockItems(RegisterEvent event) {
		if (event.getRegistryKey() == Registries.ITEM) {
			BuiltInRegistries.BLOCK.entrySet().forEach(entry -> {
				if (entry.getValue() instanceof BlockItemProvider provider) {
					event.register(Registries.ITEM, entry.getKey().location(), provider::createItem);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			RegisterCustomLecternMenuEvent.EVENT.invoker().registerLecternMenus(new RegisterCustomLecternMenuEvent());
		});
	}
}
