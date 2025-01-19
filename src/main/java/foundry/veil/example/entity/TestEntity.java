package foundry.veil.example.entity;

import foundry.veil.api.client.necromancer.SkeletonParent;
import foundry.veil.api.client.necromancer.animation.Animator;
import foundry.veil.example.client.render.entity.TestEntitySkeleton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TestEntity extends Entity implements SkeletonParent<TestEntity, TestEntitySkeleton> {

    private TestEntitySkeleton skeleton;
    private Animator<TestEntity, TestEntitySkeleton> animator;
    private int animationTicks;

    public TestEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.animationTicks = level.random.nextInt(100);
    }

    @Override
    public void tick() {
        super.tick();
        this.animationTicks++;
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

    @Override
    public void setSkeleton(@Nullable TestEntitySkeleton skeleton) {
        this.skeleton = skeleton;
    }

    @Override
    public void setAnimator(@Nullable Animator<TestEntity, TestEntitySkeleton> animator) {
        this.animator = animator;
    }

    @Override
    public @Nullable TestEntitySkeleton getSkeleton() {
        return this.skeleton;
    }

    @Override
    public @Nullable Animator<TestEntity, TestEntitySkeleton> getAnimator() {
        return this.animator;
    }

    public int getAnimationTicks() {
        return this.animationTicks;
    }
}
