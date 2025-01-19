package foundry.veil.example.entity;

import foundry.veil.api.client.necromancer.SkeletonParent;
import foundry.veil.api.client.necromancer.animation.Animator;
import foundry.veil.example.client.render.entity.mogul.MogulSkeleton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MogulEntity extends Entity implements SkeletonParent<MogulEntity, MogulSkeleton> {

    private MogulSkeleton skeleton;
    private Animator<MogulEntity, MogulSkeleton> animator;

    public MogulEntity(EntityType<? extends MogulEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setSkeleton(@Nullable MogulSkeleton skeleton) {
        this.skeleton = skeleton;
    }
    @Override
    public void setAnimator(@Nullable Animator<MogulEntity, MogulSkeleton> animator) {
        this.animator = animator;
    }
    @Override
    public @Nullable MogulSkeleton getSkeleton() {
        return this.skeleton;
    }
    @Override
    public @Nullable Animator<MogulEntity, MogulSkeleton> getAnimator() {
        return this.animator;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
