package dev.upcraft.sparkweave.mixin.ext.datagen;

import dev.upcraft.sparkweave.api.util.ext.datagen.AdvancementBuilderExt;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(Advancement.Builder.class)
public abstract class AdvancementBuilderMixin implements AdvancementBuilderExt {
    @Shadow
    public abstract Advancement save(Consumer<Advancement> consumer, String id);

    @Override
    public Advancement save(Consumer<Advancement> consumer, ResourceLocation id) {
        return save(consumer, id.toString());
    }
}
