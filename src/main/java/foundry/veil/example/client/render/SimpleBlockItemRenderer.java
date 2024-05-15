package foundry.veil.example.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SimpleBlockItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    private final BlockEntity be;

    public SimpleBlockItemRenderer(BlockEntity be) {
        this.be = be;
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.be, poseStack, source, light, overlay);
    }
}
