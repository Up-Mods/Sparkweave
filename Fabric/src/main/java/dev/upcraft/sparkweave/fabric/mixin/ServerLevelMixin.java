package dev.upcraft.sparkweave.fabric.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.upcraft.sparkweave.event.EntityTickEventsImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
	protected ServerLevelMixin(WritableLevelData $$0, ResourceKey<Level> $$1, RegistryAccess $$2, Holder<DimensionType> $$3, Supplier<ProfilerFiller> $$4, boolean $$5, boolean $$6, long $$7, int $$8) {
		super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8);
	}

	@WrapWithCondition(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
	private boolean startOfTick(Entity entity, @Share("entity") LocalRef<Entity> ref) {
		ref.set(entity);
		return !EntityTickEventsImpl.getStartHandler(entity.getClass()).invoker().startTick(entity, this);
	}

	@Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
	private void endOfTick(CallbackInfo info, @Share("entity") LocalRef<Entity> ref) {
		Entity entity = ref.get();
		EntityTickEventsImpl.getEndHandler(entity.getClass()).invoker().endTick(entity, this);
	}
}
