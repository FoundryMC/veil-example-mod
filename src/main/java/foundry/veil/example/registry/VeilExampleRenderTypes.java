package foundry.veil.example.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import foundry.veil.api.client.registry.RenderTypeStageRegistry;
import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.example.VeilExampleMod;
import foundry.veil.example.client.render.MirrorBlockEntityRenderer;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public final class VeilExampleRenderTypes extends RenderType {

    private static final ShaderStateShard MAP_SHADER = VeilRenderBridge.shaderState(VeilExampleMod.path("map"));
    private static final ShaderStateShard MAP_TEXTURE_SHADER = VeilRenderBridge.shaderState(VeilExampleMod.path("map_texture"));
    private static final ShaderStateShard MIRROR_SHADER = VeilRenderBridge.shaderState(VeilExampleMod.path("mirror"));
    private static final ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER = VeilRenderBridge.shaderState(VeilExampleMod.path("entity/rendertype_entity_cutout_no_cull"));

    private static final RenderType HEIGHTMAP_TEXTURE = create(
            VeilExampleMod.MODID + ":heightmap_texture",
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
            VeilExampleMod.MODID + ":heightmap_tessellation",
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
    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL = Util.memoize(((texture, outline) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(outline);
        RenderType rendertype = create("entity_tessellation_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, compositeState);
        RenderTypeStageRegistry.addStage(rendertype, VeilRenderBridge.patchState(4));
        return rendertype;
    }));
    private static final RenderType MIRROR = create(
            "mirror",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            TRANSIENT_BUFFER_SIZE,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(MIRROR_SHADER)
                    .setLayeringState(POLYGON_OFFSET_LAYERING)
                    .createCompositeState(true));

    static {
        RenderTypeStageRegistry.addStage(HEIGHTMAP_TESSELLATION, VeilRenderBridge.patchState(4));
    }

    private VeilExampleRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public static RenderType cursedTessellationRenderTypeEntityCutoutNoCullWhyIsThisInAFundyVideoIBetHeWillProbablySayHeIsTheOneWhoCodedItInMinecraft(ResourceLocation texture, boolean outline) {
        return ENTITY_CUTOUT_NO_CULL.apply(texture, outline);
    }

    public static RenderType mirror() {
        return MIRROR;
    }

    public static RenderType heightmap(boolean tessellation) {
        return tessellation ? HEIGHTMAP_TESSELLATION : HEIGHTMAP_TEXTURE;
    }
}
