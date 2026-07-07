package dev.upcraft.sparkweave.neoforge.impl.registry;

import dev.upcraft.sparkweave.api.client.event.RegisterParticleProvidersEvent;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

public record RegisterParticleProvidersEventImpl(
	net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent event) implements RegisterParticleProvidersEvent {

	@Override
	public <OPT extends ParticleOptions, TYPE extends ParticleType<OPT>> void registerSpecial(Supplier<TYPE> type, ParticleProvider<OPT> provider) {
		event.registerSpecial(type.get(), provider);
	}

	@Override
	public <OPT extends ParticleOptions, TYPE extends ParticleType<OPT>> void registerSpriteSet(Supplier<TYPE> type, SpriteParticleRegistration<OPT> registration) {
		event.registerSpriteSet(type.get(), registration);
	}
}
