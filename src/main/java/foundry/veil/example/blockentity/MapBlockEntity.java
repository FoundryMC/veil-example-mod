package foundry.veil.example.blockentity;

import foundry.veil.example.registry.VeilExampleBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MapBlockEntity extends BlockEntity {

    private final boolean applyScale;

    public MapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(VeilExampleBlocks.MAP_BE, blockPos, blockState);
        this.applyScale = true;
    }

    public MapBlockEntity() {
        super(VeilExampleBlocks.MAP_BE, BlockPos.ZERO, VeilExampleBlocks.MAP.defaultBlockState());
        this.applyScale = false;
    }

    public boolean isApplyScale() {
        return this.applyScale;
    }
}
