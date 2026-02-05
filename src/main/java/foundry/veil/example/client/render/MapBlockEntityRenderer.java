package foundry.veil.example.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import foundry.veil.api.client.render.shader.uniform.ShaderUniform;
import foundry.veil.api.client.render.vertex.VertexArray;
import foundry.veil.example.blockentity.MapBlockEntity;
import foundry.veil.example.editor.VeilExampleModInspector;
import foundry.veil.example.registry.VeilExampleRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL32C;

import static org.lwjgl.opengl.GL11C.*;

public class MapBlockEntityRenderer implements BlockEntityRenderer<MapBlockEntity> {

    private final VertexArray vao;

    public MapBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.vao = VertexArray.create();
        this.vao.upload(render(20), VertexArray.DrawUsage.STATIC);
        VertexArray.unbind();
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

        RenderStateShard.ShaderStateShard shard = VeilRenderType.getShards(renderType).shaderState();
        shard.setupRenderState();

        ShaderProgram shader = VeilRenderSystem.getShader();
        if (shader == null) {
            shard.clearRenderState();
            return;
        }

        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
//        modelViewStack.scale(25, 20, 25);

        this.vao.bind();

        ShaderUniform scale = shader.getUniform("Scale");
        if (scale != null) {
            scale.setVector(VeilExampleModInspector.getScale());
        }
        if (VeilExampleModInspector.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL11C.GL_LINE);
        }

        glEnable(GL32C.GL_DEPTH_CLAMP);
        this.vao.drawWithRenderType(renderType);
        glDisable(GL32C.GL_DEPTH_CLAMP);

        if (VeilExampleModInspector.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

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
