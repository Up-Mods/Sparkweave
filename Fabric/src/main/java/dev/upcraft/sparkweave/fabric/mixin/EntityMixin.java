package dev.upcraft.sparkweave.fabric.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.upcraft.sparkweave.event.EntityTickEventsImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	public abstract Level level();

	@WrapWithCondition(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
	private boolean startOfTick(Entity entity, @Share("entity") LocalRef<Entity> ref) {
		ref.set(entity);
		return !EntityTickEventsImpl.getStartHandler(entity.getClass()).invoker().startTick(entity, this.level());
	}

	@Inject(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
	private void endOfTick(CallbackInfo info, @Share("entity") LocalRef<Entity> ref) {
		Entity entity = ref.get();
		EntityTickEventsImpl.getEndHandler(entity.getClass()).invoker().endTick(entity, this.level());
	}
}
