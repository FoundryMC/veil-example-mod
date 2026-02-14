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
        RenderSystem.applyModelViewMatrix();

        this.vao.bind();
        if (VeilExampleModInspector.isRegenerateMesh()) {
            this.vao.upload(render(VeilExampleModInspector.getBaseResolution()), VertexArray.DrawUsage.STATIC);
            VeilExampleModInspector.setRegenerateMesh(false);
        }

        ShaderUniform scale = shader.getUniform("Scale");
        if (scale != null) {
            if (blockEntity.isApplyScale()) {
                scale.setVector(VeilExampleModInspector.getScale());
            } else {
                scale.setVector(1.0F, 0.25F, 1.0F);
            }
        }
        if (VeilExampleModInspector.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL11C.GL_LINE);
        }

        this.vao.drawWithRenderType(renderType);

        if (VeilExampleModInspector.tessellationWireframe()) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

        VertexBuffer.unbind();

        modelViewStack.popMatrix();
        RenderSystem.applyModelViewMatrix();
    }

    private static MeshData render(int resolution) {
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        for (int z = 0; z < resolution; z++) {
            for (int x = 0; x < resolution; x++) {
                builder.addVertex(x / (float) resolution, 0, z / (float) resolution)
                        .setUv(x / (float) resolution, z / (float) resolution);

                builder.addVertex((x + 1) / (float) resolution, 0, z / (float) resolution)
                        .setUv((x + 1) / (float) resolution, z / (float) resolution);

                builder.addVertex((x + 1) / (float) resolution, 0, (z + 1) / (float) resolution)
                        .setUv((x + 1) / (float) resolution, (z + 1) / (float) resolution);

                builder.addVertex(x / (float) resolution, 0, (z + 1) / (float) resolution)
                        .setUv(x / (float) resolution, (z + 1) / (float) resolution);
            }
        }

        return builder.buildOrThrow();
    }
}
