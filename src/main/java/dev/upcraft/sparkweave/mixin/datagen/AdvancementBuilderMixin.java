package dev.upcraft.sparkweave.mixin.datagen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.upcraft.sparkweave.util.SparkweaveLogging;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Set;

@Mixin(Advancement.Builder.class)
public class AdvancementBuilderMixin {

	/**
	 * vanilla always returns null here, making it impossible to set advancement parents via ID instead of directly.
	 * We bypass this and log a warning instead, because we cannot resolve the actual advancement at this time.
	 */
	@ModifyReturnValue(method = "method_702", at = @At("RETURN"))
	private static Advancement canActuallyBuild(Advancement _alwaysNull, ResourceLocation advancementId) {
		SparkweaveLogging.getLogger().info("Unable to verify parent advancement {}, forcing build to continue anyways.", advancementId);
		return new Advancement(advancementId, null, null, AdvancementRewards.EMPTY, Map.of(), RequirementsStrategy.AND.createRequirements(Set.of()), false);
	}

}
