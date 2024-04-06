package foundry.veil.example.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import foundry.veil.example.blockentity.MapBlockEntity;
import foundry.veil.example.registry.VeilExampleBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MapBlockItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    private static final MapBlockEntity BE = new MapBlockEntity(BlockPos.ZERO, VeilExampleBlocks.MAP.defaultBlockState());

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(BE, poseStack, source, light, overlay);
    }
}
