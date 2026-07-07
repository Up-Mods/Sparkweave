package dev.upcraft.sparkweave.api.client.event;

import dev.upcraft.sparkweave.api.event.Event;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

public interface RegisterParticleProvidersEvent {

	Event<Callback> EVENT = Event.create(Callback.class, callbacks -> event -> {
		for (Callback callback : callbacks) {
			callback.registerParticleFactories(event);
		}
	});

	<OPT extends ParticleOptions, TYPE extends ParticleType<OPT>>

	void registerSpecial(Supplier<TYPE> type, ParticleProvider<OPT> provider);

	<OPT extends ParticleOptions, TYPE extends ParticleType<OPT>> void registerSpriteSet(Supplier<TYPE> type, SpriteParticleRegistration<OPT> registration);

	@FunctionalInterface
	interface Callback {
		void registerParticleFactories(RegisterParticleProvidersEvent event);
	}

	@FunctionalInterface
	interface SpriteParticleRegistration<T extends ParticleOptions> extends ParticleResources.SpriteParticleRegistration<T> {
	}
}
