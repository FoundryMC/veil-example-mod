package foundry.veil.example;

import foundry.veil.example.client.render.MapBlockEntityRenderer;
import foundry.veil.example.client.render.MapBlockItemRenderer;
import foundry.veil.example.editor.VeilExampleModEditor;
import foundry.veil.example.registry.VeilExampleBlocks;
import foundry.veil.fabric.event.FabricVeilRendererEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class VeilExampleModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(VeilExampleBlocks.MAP, new MapBlockItemRenderer());
        BlockEntityRenderers.register(VeilExampleBlocks.MAP_BE, MapBlockEntityRenderer::new);
        FabricVeilRendererEvent.EVENT.register(renderer -> renderer.getEditorManager().add(new VeilExampleModEditor()));
    }
}