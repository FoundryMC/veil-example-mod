package foundry.veil.example.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import foundry.veil.example.blockentity.MapBlockEntity;
import foundry.veil.example.editor.VeilExampleModInspector;
import foundry.veil.example.registry.VeilExampleRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL32C;

import static org.lwjgl.opengl.GL11C.*;

public class MapBlockEntityRenderer implements BlockEntityRenderer<MapBlockEntity> {

    private final VertexBuffer vbo;

    public MapBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.vbo = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.vbo.bind();
        this.vbo.upload(render(20));
        VertexBuffer.unbind();
    }

    @Override
    public boolean shouldRenderOffScreen(MapBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public void render(MapBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
        RenderType renderType = VeilExampleRenderTypes.heightmap(VeilExampleModInspector.useTessellation());
        if (renderType == null) {
            return;
        }

        renderType.setupRenderState();
        ShaderProgram shader = VeilRenderSystem.getShader();
        if (shader == null) {
            renderType.clearRenderState();
            return;
        }

        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
//        modelViewStack.scale(25, 20, 25);

        this.vbo.bind();
        this.vbo.upload(render(20));

        shader.bind();
        shader.setVector("Scale", VeilExampleModInspector.getScale());
        if (VeilExampleModInspector.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL11C.GL_LINE);
        }

        glEnable(GL32C.GL_DEPTH_CLAMP);
        this.vbo.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        glDisable(GL32C.GL_DEPTH_CLAMP);

        if (VeilExampleModInspector.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        renderType.clearRenderState();

        VertexBuffer.unbind();

        modelViewStack.popMatrix();
    }

    private static MeshData render(int resolution) {
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        for (int z = 0; z <= resolution - 1; z++) {
            for (int x = 0; x <= resolution - 1; x++) {
                builder.addVertex(x / (float) resolution, 0, z / (float) resolution)
                        .setUv(x / (float) resolution, z / (float) resolution);

                builder.addVertex((x + 1) / (float) resolution, 0, z / (float) resolution)
                        .setUv((x + 1) / (float) resolution, z / (float) resolution);

                builder.addVertex(x / (float) resolution, 0, (z + 1) / (float) resolution)
                        .setUv(x / (float) resolution, (z + 1) / (float) resolution);

                builder.addVertex((x + 1) / (float) resolution, 0, (z + 1) / (float) resolution)
                        .setUv((x + 1) / (float) resolution, (z + 1) / (float) resolution);
            }
        }

        return builder.buildOrThrow();
    }
}
