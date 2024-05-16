package foundry.veil.example;

import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.example.blockentity.MapBlockEntity;
import foundry.veil.example.client.render.MapBlockEntityRenderer;
import foundry.veil.example.client.render.MirrorBlockEntityRenderer;
import foundry.veil.example.client.render.SimpleBlockItemRenderer;
import foundry.veil.example.editor.VeilExampleModEditor;
import foundry.veil.example.registry.VeilExampleBlocks;
import foundry.veil.fabric.event.FabricVeilRenderLevelStageEvent;
import foundry.veil.fabric.event.FabricVeilRendererEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;

public class VeilExampleModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(VeilExampleBlocks.MAP, new SimpleBlockItemRenderer(new MapBlockEntity(BlockPos.ZERO, VeilExampleBlocks.MAP.defaultBlockState())));
        BlockEntityRenderers.register(VeilExampleBlocks.MAP_BE, MapBlockEntityRenderer::new);
        BlockEntityRenderers.register(VeilExampleBlocks.MIRROR_BE, MirrorBlockEntityRenderer::new);
        FabricVeilRendererEvent.EVENT.register(renderer -> renderer.getEditorManager().add(new VeilExampleModEditor()));

        FabricVeilRenderLevelStageEvent.EVENT.register((stage, levelRenderer, bufferSource, poseStack, projectionMatrix, renderTick, partialTicks, camera, frustum) -> {
            if (stage == VeilRenderLevelStageEvent.Stage.AFTER_LEVEL) {
                MirrorBlockEntityRenderer.renderLevel(Minecraft.getInstance().level, bufferSource, poseStack, projectionMatrix, partialTicks, VeilRenderBridge.create(frustum), camera);
            }
        });
    }
}