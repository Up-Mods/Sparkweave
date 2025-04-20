package dev.upcraft.sparkweave.testmod;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.EntityTickEvents;
import dev.upcraft.sparkweave.api.event.ItemMenuInteractionEvent;
import dev.upcraft.sparkweave.api.event.RegisterCustomLecternMenuEvent;
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
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

@AutoService(MainEntryPoint.class)
public class SparkweaveTestmod implements MainEntryPoint {

	public static final String MODID = "sparkweave_testmod";

	@Override
	public void onInitialize(ModContainer mod) {
		var registryService = RegistryService.get();

		TestItems.ITEMS.accept(registryService);
		TestCreativeTabs.TABS.accept(registryService);
		TestStatusEffects.STATUS_EFFECTS.accept(registryService);

		EntityTickEvents.startTick(Boat.class).register((boat, level) -> {
			if (!level.isClientSide() && boat.getControllingPassenger() instanceof Player player && player.getMainHandItem().is(Items.BEACON)) {
				player.displayClientMessage(Component.literal("Start of Boat server tick"), true);
			}

			return false;
		});

		EntityTickEvents.endTick(Minecart.class).register((minecart, level) -> {
			if (level.isClientSide() && minecart.getFirstPassenger() instanceof Player player && player.getMainHandItem().is(Items.BEACON)) {
				player.displayClientMessage(Component.literal("End of Minecart client tick"), true);
			}
		});

		ItemMenuInteractionEvent.EVENT.register((menu, player, level, clickAction, slot, slotStack, cursorStack) -> {
			if (menu instanceof ChestMenu && clickAction == ClickAction.SECONDARY && slotStack.is(Items.DEEPSLATE_COAL_ORE) && cursorStack.is(Items.DEEPSLATE_EMERALD_ORE) && player.isCrouching()) {
				player.displayClientMessage(Component.literal("Uh oh stinky"), false);
				level.playSound(null, player.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS);
				return true;
			}

			return false;
		});

		RegisterCustomLecternMenuEvent.EVENT.register(event -> event.register((level, pos, player, blockEntity, stack) -> new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return Component.empty();
			}

			@Override
			public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
				return MenuType.GENERIC_3x3.create(i, inventory);
			}
		}, TestItems.TEST_ITEM));
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
