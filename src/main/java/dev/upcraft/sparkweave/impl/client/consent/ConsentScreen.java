package dev.upcraft.sparkweave.impl.client.consent;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ConsentScreen extends Screen {

	private static final Component TITLE = Component.translatable("screen.sparkweave.consent.title");
	private final List<ResourceLocation> permissions;
	private final boolean explicit;

	public ConsentScreen(List<ResourceLocation> permissions, boolean explicit) {
		super(TITLE);
		this.permissions = permissions;
		this.explicit = explicit;
		System.out.println("screen constructor");
	}

	@Override
	protected void init() {
		super.init();
		System.out.println("initializing screen");
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(guiGraphics, mouseX, mouseY, delta);
		super.render(guiGraphics, mouseX, mouseY, delta);
		System.out.println("rendering screen");
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
