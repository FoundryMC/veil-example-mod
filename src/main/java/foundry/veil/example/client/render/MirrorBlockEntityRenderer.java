package foundry.veil.example.client.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import foundry.veil.api.client.render.CullFrustum;
import foundry.veil.api.client.render.VeilLevelPerspectiveRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.VeilRenderer;
import foundry.veil.api.client.render.framebuffer.AdvancedFbo;
import foundry.veil.api.client.render.framebuffer.FramebufferManager;
import foundry.veil.example.VeilExampleMod;
import foundry.veil.example.block.MirrorBlock;
import foundry.veil.example.blockentity.MirrorBlockEntity;
import foundry.veil.example.registry.VeilExampleBlocks;
import foundry.veil.example.registry.VeilExampleRenderTypes;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeResource;

import java.nio.IntBuffer;
import java.util.Map;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;

public class MirrorBlockEntityRenderer implements BlockEntityRenderer<MirrorBlockEntity>, NativeResource {

    private static final ResourceLocation[] MIRROR_FBOS = IntStream.range(0, 4).mapToObj(i -> VeilExampleMod.path("mirror" + i)).toArray(ResourceLocation[]::new);
    private static final float RENDER_DISTANCE = 64.0F;

    private static final Matrix4f RENDER_MODELVIEW = new Matrix4f();
    private static final Matrix4f RENDER_PROJECTION = new Matrix4f();

    private static final ObjectSet<BlockPos> RENDER_POSITIONS = new ObjectArraySet<>();
    private static final Object2ObjectMap<BlockPos, MirrorTexture> TEXTURES = new Object2ObjectArrayMap<>();

    public MirrorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> client.execute(this::free));
    }

    @Override
    public void render(MirrorBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockPos pos = blockEntity.getBlockPos();
        RENDER_POSITIONS.add(pos);
        MirrorTexture texture = TEXTURES.get(pos);
        if (texture == null) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Direction facing = blockEntity.getBlockState().getValue(MirrorBlock.FACING);
        poseStack.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
        poseStack.translate(-0.5, -0.5, -0.5);

        Matrix4f pose = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        builder.vertex(pose, 0, 0, 0.125F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F).overlayCoords(j).uv2(i).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, 1, 0, 0.125F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 1.0F).overlayCoords(j).uv2(i).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, 1, 1, 0.125F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 0.0F).overlayCoords(j).uv2(i).normal(0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, 0, 1, 0.125F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0.0F).overlayCoords(j).uv2(i).normal(0.0F, 0.0F, 1.0F).endVertex();

        RenderSystem.setShaderTexture(0, texture.getId());
        VeilExampleRenderTypes.mirror().end(builder, RenderSystem.getVertexSorting());

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 64;
    }

    @Override
    public boolean shouldRender(MirrorBlockEntity blockEntity, Vec3 vec3) {
        BlockPos pos = blockEntity.getBlockPos();
        int viewDistance = this.getViewDistance();
        if (vec3.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) >= viewDistance * viewDistance) {
            return false;
        }

        CullFrustum frustum = VeilRenderer.getCullingFrustum();
        Direction facing = blockEntity.getBlockState().getValue(MirrorBlock.FACING);
        Vec3i normal = facing.getNormal();
        if (dot(pos, normal, vec3.x, vec3.y, vec3.z) >= 0) {
            return false;
        }

        AABB box = MirrorBlock.BOUNDING_BOXES[facing.get2DDataValue()];
        return frustum.testAab(
                pos.getX() + box.minX,
                pos.getY() + box.minY,
                pos.getZ() + box.minZ,
                pos.getX() + box.maxX,
                pos.getY() + box.maxY,
                pos.getZ() + box.maxZ);
    }

    private static float dot(BlockPos pos, Vec3i normal, double x, double y, double z) {
        return (float) ((pos.getX() + 0.5 - normal.getX() * 0.5 - x) * normal.getX() + (pos.getY() + 0.5 - normal.getY() * 0.5 - y) * normal.getY() + (pos.getZ() + 0.5 - normal.getZ() * 0.5 - z) * normal.getZ());
    }

    public static void renderLevel(ClientLevel level, Matrix4fc projection, float partialTicks, CullFrustum frustum) {
        if (VeilLevelPerspectiveRenderer.isRenderingPerspective() || !projection.isFinite()) {
            return;
        }

        FramebufferManager framebufferManager = VeilRenderSystem.renderer().getFramebufferManager();
        AdvancedFbo baseFbo = framebufferManager.getFramebuffer(MIRROR_FBOS[0]);
        if (baseFbo == null) {
            return;
        }

        for (BlockPos pos : RENDER_POSITIONS) {
            BlockState state = level.getBlockState(pos);
            if (!state.is(VeilExampleBlocks.MIRROR)) {
                continue;
            }

            Direction facing = state.getValue(MirrorBlock.FACING);
            Vec3i normal = facing.getNormal();
            Vector3dc cameraPos = frustum.getPosition();
            int lod = Mth.clamp(0, (int) (cameraPos.distanceSquared(pos.getX(), pos.getY(), pos.getZ()) / 64.0), MIRROR_FBOS.length - 1);
            AdvancedFbo fbo = framebufferManager.getFramebuffer(MIRROR_FBOS[lod]);
            if (fbo == null) {
                fbo = baseFbo;
            }

            MirrorTexture mirror = TEXTURES.computeIfAbsent(pos, unused -> new MirrorTexture());
            Vector3d renderPos = new Vector3d(pos.getX() + 0.5 - normal.getX() * 0.5, pos.getY() + 0.5 - normal.getY() * 0.5, pos.getZ() + 0.5 - normal.getZ() * 0.5);
            VeilLevelPerspectiveRenderer.render(fbo, RENDER_MODELVIEW, RENDER_PROJECTION.setPerspective(30, (float) fbo.getWidth() / fbo.getHeight(), 0.3F, RENDER_DISTANCE * 4.0F), renderPos, Axis.YN.rotationDegrees(180 - facing.toYRot()), RENDER_DISTANCE, partialTicks);
            mirror.copy(fbo);
        }

        ObjectIterator<Map.Entry<BlockPos, MirrorTexture>> iterator = TEXTURES.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, MirrorTexture> entry = iterator.next();
            if (!RENDER_POSITIONS.contains(entry.getKey())) {
                iterator.remove();
                entry.getValue().close();
            }
        }

        RENDER_POSITIONS.clear();
    }

    @Override
    public void free() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ObjectCollection<MirrorTexture> values = TEXTURES.values();
            IntBuffer textures = stack.mallocInt(values.size());
            values.forEach(texture -> textures.put(texture.getId()));
            textures.flip();
            glDeleteTextures(textures);
        }
        TEXTURES.clear();
    }

    private static class MirrorTexture extends AbstractTexture {

        private int width;
        private int height;

        private MirrorTexture() {
            this.setFilter(false, true);
            this.width = -1;
            this.height = -1;
        }

        @Override
        public void load(ResourceManager resourceManager) {
        }

        public void copy(AdvancedFbo fbo) {
            int id = this.getId();
            int width = fbo.getWidth();
            int height = fbo.getHeight();
            if (this.width != width || this.height != height) {
                this.width = width;
                this.height = height;
                TextureUtil.prepareImage(NativeImage.InternalGlFormat.RGBA, id, 4, width, height);
            }

            RenderSystem.bindTexture(id);
            fbo.bindRead();
            glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height);
            AdvancedFbo.unbind();
            glGenerateMipmap(GL_TEXTURE_2D);
        }
    }
}
