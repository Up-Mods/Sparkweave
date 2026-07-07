package dev.upcraft.sparkweave.testmod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

public class ExperiencedStatusEffect extends MobEffect {

	public ExperiencedStatusEffect(MobEffectCategory category, int particleColor) {
		super(category, particleColor);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
		return tickCount % 40 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
		for (int i = 0; i < amplification + 3; i++) {
			var entities = serverLevel.getEntities((Entity) null, AABB.ofSize(mob.position(), 15.0D, 5.0D, 15.0D), ((Predicate<Entity>) (entity -> entity.isAlive() && entity instanceof Player)).and(EntitySelector.NO_CREATIVE_OR_SPECTATOR));
			var player = (Player) Util.getRandom(entities, mob.getRandom());
			var amount = player.getRandom().nextInt(7);
			player.giveExperiencePoints(amount);
		}

		return true;
	}
}
