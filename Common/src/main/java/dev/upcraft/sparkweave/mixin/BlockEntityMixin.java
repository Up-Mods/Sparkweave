package dev.upcraft.sparkweave.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
	// To hopefully ensure a higher degree of compatibility since LecternBlockEntity doesn't override these methods

	@Shadow
	public abstract CompoundTag saveWithoutMetadata(HolderLookup.Provider registries);

	@ModifyReturnValue(method = "getUpdatePacket", at = @At("RETURN"))
	private Packet<ClientGamePacketListener> setLecternPacket(Packet<ClientGamePacketListener> original) {
		return (Object) this instanceof LecternBlockEntity lectern ? ClientboundBlockEntityDataPacket.create(lectern) : original;
	}

	@ModifyReturnValue(method = "getUpdateTag", at = @At("RETURN"))
	private CompoundTag setLecternUpdateTag(CompoundTag original, HolderLookup.Provider provider) {
		if((Object) this instanceof LecternBlockEntity) {
			return this.saveWithoutMetadata(provider).merge(original);
		}

		return original;
	}
}
