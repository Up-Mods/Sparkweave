package dev.upcraft.sparkweave.testmod.datacomponent;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Openable(boolean isOpen) {

	public static final Codec<Openable> CODEC = Codec.BOOL.xmap(Openable::new, Openable::isOpen);
	public static final StreamCodec<ByteBuf, Openable> STREAM_CODEC = ByteBufCodecs.BOOL.map(Openable::new, Openable::isOpen);

	public static final Openable OPEN = new Openable(true);
	public static final Openable CLOSED = new Openable(false);

	public Openable toggle() {
		return new Openable(!this.isOpen);
	}
}
