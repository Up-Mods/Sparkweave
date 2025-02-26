package dev.upcraft.sparkweave.neoforge.mixin.datagen;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Deprecated(forRemoval = true)
@Mixin(GatherDataEvent.class)
public interface GatherDataEventAccessor {

	@Accessor("config")
	GatherDataEvent.DataGeneratorConfig sparkweave$getConfig();
}
