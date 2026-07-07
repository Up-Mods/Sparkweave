package dev.upcraft.sparkweave.fabric.impl.registry;

import dev.upcraft.sparkweave.api.client.event.RegisterLayerDefinitionsEvent;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class RegisterLayerDefinitionsEventImpl implements RegisterLayerDefinitionsEvent {

	@Override
	public void registerModelLayers(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
		ModelLayerRegistry.registerModelLayer(layerLocation, supplier::get);
	}
}
