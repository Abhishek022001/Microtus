package net.minestom.server.entity.metadata.projectile;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.MetadataHolder;
import net.minestom.server.entity.metadata.EntityMeta;
import org.jetbrains.annotations.NotNull;

public class AbstractArrowMeta extends EntityMeta {
    public static final byte OFFSET = EntityMeta.MAX_OFFSET;
    public static final byte MAX_OFFSET = OFFSET + 2;

    //Microtus start - update java keyword usage
    private static final byte CRITICAL_BIT = 0x01;
    private static final byte NO_CLIP_BIT = 0x02;
    //Microtus end - update java keyword usage

    protected AbstractArrowMeta(@NotNull Entity entity, @NotNull MetadataHolder metadata) {
        super(entity, metadata);
    }

    public boolean isCritical() {
        return getMaskBit(OFFSET, CRITICAL_BIT);
    }

    public void setCritical(boolean value) {
        setMaskBit(OFFSET, CRITICAL_BIT, value);
    }

    public boolean isNoClip() {
        return getMaskBit(OFFSET, NO_CLIP_BIT);
    }

    public void setNoClip(boolean value) {
        setMaskBit(OFFSET, NO_CLIP_BIT, value);
    }

    public byte getPiercingLevel() {
        return super.metadata.getIndex(OFFSET + 1, (byte) 0);
    }

    public void setPiercingLevel(byte value) {
        super.metadata.setIndex(OFFSET + 1, Metadata.Byte(value));
    }

}
