package dev.upcraft.sparkweave.client.consent;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ConsentScreen extends Screen {

	private static final Component TITLE = Component.translatable("screen.sparkweave.consent.title");
	private final List<Identifier> permissions;
	private final boolean explicit;

	public ConsentScreen(List<Identifier> permissions, boolean explicit) {
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
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);
		System.out.println("rendering screen");
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
