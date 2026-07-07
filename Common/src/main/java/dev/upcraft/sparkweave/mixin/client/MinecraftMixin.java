package dev.upcraft.sparkweave.mixin.client;

import dev.upcraft.sparkweave.api.client.ext.MinecraftExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.item.ItemModelResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements MinecraftExt {

	@Accessor("blockModelResolver")
	@Override
	public abstract BlockModelResolver sparkweave$getBlockModelResolver();

	@Accessor("itemModelResolver")
	@Override
	public abstract ItemModelResolver sparkweave$getItemModelResolver();
}
