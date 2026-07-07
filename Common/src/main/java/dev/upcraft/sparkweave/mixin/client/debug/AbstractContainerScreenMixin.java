package dev.upcraft.sparkweave.mixin.client.debug;

import com.llamalad7.mixinextras.sugar.Local;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {

	private AbstractContainerScreenMixin(Component title) {
		super(title);
		throw new UnsupportedOperationException();
	}

	@Inject(method = "extractSlots", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;extractSlot(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/inventory/Slot;II)V", shift = At.Shift.AFTER))
	private void renderSlotNumbers(GuiGraphicsExtractor graphics, int mouseX, int mouseY, CallbackInfo ci, @Local(name = "slot") Slot slot) {
		if(SparkweaveApi.Client.RENDER_SLOT_NUMBERS) {
			graphics.nextStratum();
			var poseStack = graphics.pose();
			poseStack.pushMatrix();
			poseStack.scale(0.5F, 0.5F);
			graphics.text(this.font, String.valueOf(slot.index), 2, 2, 0xA7FFFFFF);
			// TODO show source container and container slot ID on hover
//			guiGraphics.drawString(this.font, Component.literal(String.valueOf(slot.index)).append(" ").append(Component.literal(String.valueOf(slot.getContainerSlot())).withStyle(ChatFormatting.YELLOW)), 2, 2, 0xBFFFFFFF);
			poseStack.popMatrix();
		}
	}
}
