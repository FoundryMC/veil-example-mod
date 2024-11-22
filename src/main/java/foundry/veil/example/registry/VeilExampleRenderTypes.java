package foundry.veil.example.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import foundry.veil.api.client.registry.RenderTypeStageRegistry;
import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.example.VeilExampleMod;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public final class VeilExampleRenderTypes extends RenderType {

    private static final ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER = VeilRenderBridge.shaderState(VeilExampleMod.path("entity/rendertype_entity_cutout_no_cull"));

    private static final ResourceLocation HEIGHTMAP_TEXTURE = VeilExampleMod.path("heightmap_texture");
    private static final ResourceLocation HEIGHTMAP_TESSELLATION = VeilExampleMod.path("heightmap_tessellation");
    private static final ResourceLocation MIRROR = VeilExampleMod.path("mirror");

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL = Util.memoize(((texture, outline) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(outline);
        RenderType rendertype = create("entity_tessellation_cutout_no_cull",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                TRANSIENT_BUFFER_SIZE,
                true,
                false,
                compositeState);
        RenderTypeStageRegistry.addStage(rendertype, VeilRenderBridge.patchState(4));
        return rendertype;
    }));

    private VeilExampleRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public static RenderType cursedTessellationRenderTypeEntityCutoutNoCullWhyIsThisInAFundyVideoIBetHeWillProbablySayHeIsTheOneWhoCodedItInMinecraft(ResourceLocation texture, boolean outline) {
        return ENTITY_CUTOUT_NO_CULL.apply(texture, outline);
    }

    public static RenderType mirror() {
        return VeilRenderType.get(MIRROR);
    }

    public static RenderType heightmap(boolean tessellation) {
        return VeilRenderType.get(tessellation ? HEIGHTMAP_TESSELLATION : HEIGHTMAP_TEXTURE);
    }
}
