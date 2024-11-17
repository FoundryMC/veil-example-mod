package foundry.veil.example;

import com.mojang.blaze3d.platform.NativeImage;
import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.api.client.render.framebuffer.AdvancedFbo;
import foundry.veil.api.client.render.framebuffer.AdvancedFboAttachment;
import foundry.veil.api.client.render.texture.DynamicCubemapTexture;
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
import net.minecraft.core.Direction;

import java.util.Random;

public class VeilExampleModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(VeilExampleBlocks.MAP, new SimpleBlockItemRenderer(new MapBlockEntity(BlockPos.ZERO, VeilExampleBlocks.MAP.defaultBlockState())));
        BlockEntityRenderers.register(VeilExampleBlocks.MAP_BE, MapBlockEntityRenderer::new);
        BlockEntityRenderers.register(VeilExampleBlocks.MIRROR_BE, MirrorBlockEntityRenderer::new);
        FabricVeilRendererEvent.EVENT.register(renderer -> renderer.getEditorManager().add(new VeilExampleModEditor()));

        FabricVeilRenderLevelStageEvent.EVENT.register((stage, levelRenderer, bufferSource, poseStack, projectionMatrix, renderTick, partialTicks, camera, frustum) -> {
            if (stage == VeilRenderLevelStageEvent.Stage.AFTER_LEVEL) {
                MirrorBlockEntityRenderer.renderLevel(Minecraft.getInstance().level, projectionMatrix, partialTicks, VeilRenderBridge.create(frustum), camera);
            }
        });

        // Make sure there's no crash
        FabricVeilRendererEvent.EVENT.register(renderer -> {
            DynamicCubemapTexture cubemap = new DynamicCubemapTexture();
            for (Direction value : Direction.values()) {
                try (NativeImage image = genTest(64, 64)) {
                    cubemap.upload(value, image);
                }
            }
            AdvancedFbo fbo = AdvancedFbo.withSize(64, 64).addColorTextureWrapper(cubemap.getId(), 0).build(true);
            fbo.bind(false);
            fbo.setColorAttachmentTexture(0, cubemap.getId(), 1);
            AdvancedFbo.unbind();
        });
    }

    private static NativeImage genTest(int width, int height) {
        NativeImage nativeImage = new NativeImage(width, height, false);

        Random random = new Random();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                nativeImage.setPixelRGBA(x, y, 0xFF000000 | random.nextInt(0xFFFFFF));
            }
        }

        return nativeImage;
    }
}