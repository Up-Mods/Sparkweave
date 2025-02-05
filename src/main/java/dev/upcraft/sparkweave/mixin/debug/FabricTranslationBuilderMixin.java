package dev.upcraft.sparkweave.mixin.debug;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FabricLanguageProvider.TranslationBuilder.class)
public interface FabricTranslationBuilderMixin {

	@Inject(method = "add(Lnet/minecraft/world/item/enchantment/Enchantment;Ljava/lang/String;)V", at = @At("HEAD"))
	default void redirectEnchantments(Enchantment enchantment, String value, CallbackInfo ci) {
		throw new UnsupportedOperationException("[Sparkweave] Please use the alternative provided method instead, to also provide a translation for this enchantment: " + BuiltInRegistries.ENCHANTMENT.getKey(enchantment));
	}
}
