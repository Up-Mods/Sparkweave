package dev.upcraft.sparkweave.neoforge.mixin.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(RegisterMenuScreensEvent.class)
public abstract class RegisterMenuScreensEventMixin implements dev.upcraft.sparkweave.api.client.event.RegisterMenuScreensEvent {

	@Shadow
	public abstract <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor);

	@Override
	public <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> void register(Supplier<MenuType<MENU>> menuType, ScreenConstructor<MENU, SCREEN> screenFactory) {
		this.register(menuType.get(), screenFactory);
	}
}
