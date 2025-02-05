package dev.upcraft.sparkweave.mixin.datagen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.upcraft.sparkweave.util.SparkweaveLogging;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(Advancement.Builder.class)
public class AdvancementBuilderMixin {

	@Shadow
	@Nullable
	private ResourceLocation parentId;

	@WrapOperation(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/Advancement$Builder;canBuild(Ljava/util/function/Function;)Z"))
	private boolean canActuallyBuild(Advancement.Builder instance, Function<ResourceLocation, Advancement> parentLookup, Operation<Boolean> original, ResourceLocation advancementId) {
		boolean canBuild = original.call(instance, parentLookup);

		if(!canBuild && this.parentId != null && !this.parentId.getNamespace().equals(advancementId.getNamespace())) {
			SparkweaveLogging.getLogger().info("Unable to verify parent advancement {} for {}, forcing build to continue anyways.", this.parentId, advancementId);
			return true;
		}

		return canBuild;
	}

}
