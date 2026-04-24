package technology.roughness.whitenoise.config;

import org.jspecify.annotations.NonNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import technology.roughness.whitenoise.WhiteNoise;

public class WhiteNoiseConfigPayload implements CustomPacketPayload {

    public static final Type<WhiteNoiseConfigPayload> TYPE =
            new Type<>(WhiteNoise.prefix("sync"));
    public static final StreamCodec<FriendlyByteBuf, WhiteNoiseConfigPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BYTE_ARRAY,
        packet -> packet.contents,
        ByteBufCodecs.STRING_UTF8,
        packet -> packet.fileName,
        WhiteNoiseConfigPayload::new
    );

    public final String fileName;
    public final byte[] contents;

    public WhiteNoiseConfigPayload(byte[] contents, String fileName) {
        this.fileName = fileName;
        this.contents = contents;
    }

    public WhiteNoiseConfigPayload(FriendlyByteBuf buf) {
        this(buf.readByteArray(), buf.readUtf());
    }

    @NonNull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
