package dev.upcraft.sparkweave.testmod;

import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CustomLecternMenuEvent;
import dev.upcraft.sparkweave.api.event.ItemMenuInteractionEvent;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.testmod.init.TestCreativeTabs;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import dev.upcraft.sparkweave.testmod.init.TestStatusEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class SparkweaveTestmod implements MainEntryPoint {

	public static final String MODID = "sparkweave_testmod";

	@Override
	public void onInitialize(ModContainer mod) {
		var registryService = RegistryService.get();

		TestItems.ITEMS.accept(registryService);
		TestCreativeTabs.TABS.accept(registryService);
		TestStatusEffects.STATUS_EFFECTS.accept(registryService);

		ItemMenuInteractionEvent.EVENT.register((menu, player, level, clickAction, slot, slotStack, cursorStack) -> {
			if(menu instanceof ChestMenu && clickAction == ClickAction.SECONDARY && slotStack.is(Items.DEEPSLATE_COAL_ORE) && cursorStack.is(Items.DEEPSLATE_EMERALD_ORE) && player.isCrouching()) {
				player.displayClientMessage(Component.literal("Uh oh stinky"), false);
				level.playSound(null, player.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS);
				return true;
			}

			return false;
		});

		CustomLecternMenuEvent.EVENT.register(event -> event.register((level, pos, player, blockEntity, stack) -> new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return Component.empty();
			}

			@Override
			public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
				return MenuType.GENERIC_3x3.create(i, inventory);
			}
		}, Items.DIAMOND));
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
