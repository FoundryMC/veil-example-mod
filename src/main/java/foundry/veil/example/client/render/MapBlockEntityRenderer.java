package foundry.veil.example.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import foundry.veil.example.blockentity.MapBlockEntity;
import foundry.veil.example.editor.VeilExampleModEditor;
import foundry.veil.example.registry.VeilExampleRenderTypes;
import imgui.ImGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL32C;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;

public class MapBlockEntityRenderer implements BlockEntityRenderer<MapBlockEntity> {

    private final VertexBuffer vbo;

    public MapBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.vbo = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.vbo.bind();
        this.vbo.upload(render(20));
        VertexBuffer.unbind();
    }

    @Override
    public void render(MapBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
        RenderType renderType = VeilExampleRenderTypes.heightmap(VeilExampleModEditor.useTessellation());

        renderType.setupRenderState();
        ShaderProgram shader = VeilRenderSystem.getShader();
        if (shader == null) {
            renderType.clearRenderState();
            return;
        }

        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        modelViewStack.scale(25, 20, 25);

        this.vbo.bind();
        this.vbo.upload(render(20));

        shader.applyRenderSystem();
        shader.setMatrix("ModelViewMat", modelViewStack.last().pose());
        shader.setup();
        if (VeilExampleModEditor.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL11C.GL_LINE);
        }
        glEnable(GL32C.GL_DEPTH_CLAMP);
        RenderSystem.activeTexture(GL_TEXTURE0);
        RenderSystem.bindTexture(Minecraft.getInstance().getMainRenderTarget().getColorTextureId());
        this.vbo.draw();
        glDisable(GL32C.GL_DEPTH_CLAMP);
        if (VeilExampleModEditor.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        renderType.clearRenderState();

        VertexBuffer.unbind();

        modelViewStack.popPose();
    }

    private static BufferBuilder.RenderedBuffer render(int resolution) {
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        for (int z = 0; z <= resolution - 1; z++) {
            for (int x = 0; x <= resolution - 1; x++) {
                builder.vertex(x / (float) resolution, 0, z / (float) resolution)
                        .uv(x / (float) resolution, z / (float) resolution)
                        .endVertex();

                builder.vertex((x + 1) / (float) resolution, 0, z / (float) resolution)
                        .uv((x + 1) / (float) resolution, z / (float) resolution)
                        .endVertex();

                builder.vertex(x / (float) resolution, 0, (z + 1) / (float) resolution)
                        .uv(x / (float) resolution, (z + 1) / (float) resolution)
                        .endVertex();

                builder.vertex((x + 1) / (float) resolution, 0, (z + 1) / (float) resolution)
                        .uv((x + 1) / (float) resolution, (z + 1) / (float) resolution)
                        .endVertex();
            }
        }

        return builder.end();
    }
}
