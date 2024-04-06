package foundry.veil.example.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import foundry.veil.api.client.registry.RenderTypeStageRegistry;
import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.example.VeilExampleMod;
import net.minecraft.client.renderer.RenderType;

public final class VeilExampleRenderTypes extends RenderType {

    private static final ShaderStateShard MAP_SHADER = VeilRenderBridge.shaderState(VeilExampleMod.path("map"));
    private static final ShaderStateShard MAP_TEXTURE_SHADER = VeilRenderBridge.shaderState(VeilExampleMod.path("map_texture"));

    private static final RenderType HEIGHTMAP_TEXTURE = create(
            VeilExampleMod.MODID + ":heightmap",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            TRANSIENT_BUFFER_SIZE,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(MAP_TEXTURE_SHADER)
                    .setTextureState(new TextureStateShard(VeilExampleMod.path("textures/misc/heightmap.png"), true, false))
                    .createCompositeState(true)
    );
    private static final RenderType HEIGHTMAP_TESSELLATION = create(
            VeilExampleMod.MODID + ":heightmap",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            TRANSIENT_BUFFER_SIZE,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(MAP_SHADER)
                    .setTextureState(new TextureStateShard(VeilExampleMod.path("textures/misc/heightmap.png"), true, false))
                    .createCompositeState(true)
    );

    static {
        RenderTypeStageRegistry.addStage(HEIGHTMAP_TESSELLATION, VeilRenderBridge.patchState(4));
    }

    private VeilExampleRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public static RenderType heightmap(boolean tessellation) {
        return tessellation ? HEIGHTMAP_TESSELLATION : HEIGHTMAP_TEXTURE;
    }
}
