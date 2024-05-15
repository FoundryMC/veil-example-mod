package foundry.veil.example.registry;

import foundry.veil.example.VeilExampleMod;
import foundry.veil.example.block.MapBlock;
import foundry.veil.example.block.MirrorBlock;
import foundry.veil.example.blockentity.MapBlockEntity;
import foundry.veil.example.blockentity.MirrorBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class VeilExampleBlocks {

    public static final Block MAP = register("map", new MapBlock(BlockBehaviour.Properties.copy(Blocks.BEDROCK)
                    .noOcclusion()
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)),
            new Item.Properties());
    public static final Block MIRROR = register("mirror", new MirrorBlock(BlockBehaviour.Properties.copy(Blocks.BEDROCK)
                    .noOcclusion()
                    .isRedstoneConductor(Blocks::never)
                    .isSuffocating(Blocks::never)),
            new Item.Properties());
    public static final BlockEntityType<MapBlockEntity> MAP_BE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, VeilExampleMod.path("map"), BlockEntityType.Builder.of(MapBlockEntity::new, MAP).build(null));
    public static final BlockEntityType<MirrorBlockEntity> MIRROR_BE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, VeilExampleMod.path("mirror"), BlockEntityType.Builder.of(MirrorBlockEntity::new, MIRROR).build(null));

    public static void bootstrap() {
    }

    public static <T extends Block> T register(String name, T block, Item.Properties properties) {
        register(name, block);
        VeilExampleItems.register(name, new BlockItem(block, properties));
        return block;
    }

    public static Block register(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, VeilExampleMod.path(name), block);
    }
}
