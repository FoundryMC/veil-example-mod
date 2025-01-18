package foundry.veil.example.client.render.entity;

import foundry.veil.api.client.necromancer.Bone;
import foundry.veil.api.client.necromancer.animation.Animator;
import foundry.veil.api.client.necromancer.render.NecromancerEntityRenderer;
import foundry.veil.api.client.necromancer.render.NecromancerRenderer;
import foundry.veil.api.client.necromancer.render.NecromancerSkinEntityRenderLayer;
import foundry.veil.api.client.necromancer.render.Skin;
import foundry.veil.api.client.render.MatrixStack;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.example.VeilExampleMod;
import foundry.veil.example.entity.TestEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class TestEntityRenderer extends NecromancerEntityRenderer<TestEntity, TestEntitySkeleton> {

    private static final Skin TEST_SKIN = createSkin();
    private static final ResourceLocation RENDERTYPE = VeilExampleMod.path("test_entity");
    private static final ResourceLocation TEXTURE_LOCATION = VeilExampleMod.path("textures/entity/test.png");

    private final ModelPart test;

    public TestEntityRenderer(EntityRendererProvider.Context context) {
        super(context, 1.0F);
        this.addLayer(new NecromancerSkinEntityRenderLayer<>(this) {
            @Override
            public RenderType getRenderType(TestEntity entity) {
                return VeilRenderType.get(RENDERTYPE, TEXTURE_LOCATION);
            }

            @Override
            public Skin getSkin(TestEntity parent) {
                return TEST_SKIN;
            }
        });
        this.test = new ModelPart(List.of(new ModelPart.Cube(0, 0, 0, 0, 0, 4, 8, 4, 0, 0, 0, false, 16, 16, EnumSet.allOf(Direction.class))), Collections.emptyMap());
    }

    @Override
    public void render(TestEntity testEntity, NecromancerRenderer context, MatrixStack matrixStack, int packedLight, float partialTicks) {
        TestEntitySkeleton skeleton = testEntity.getSkeleton();
        if (skeleton != null) {
            for (Bone bone : skeleton.bones.values()) {
                bone.reset();
                bone.color.set(1, 1, 1, 1);

                float time = testEntity.tickCount + partialTicks;
                bone.position.y = (float) Math.sin(time * 45 * Math.PI / 180.0);
                float rot = (float) (time * 8 * Math.PI / 180.0);
                bone.rotation.rotateXYZ(rot, rot, rot);
            }
        }
        matrixStack.matrixPush();
        matrixStack.applyScale(-1.0F, -1.0F, 1.0F);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
        this.test.render(matrixStack.toPoseStack(), context.getBuffer(RenderType.entityCutout(TEXTURE_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY);
        matrixStack.matrixPop();
        super.render(testEntity, context, matrixStack, packedLight, 1.0F);
    }

    private static Skin createSkin() {
        Skin.Builder builder = Skin.builder(16, 16);

        builder.startBone("test");
        builder.addCube(4, 8, 4, 0, 0, 0, 0, 0, 0, 0, 0, false);

        return builder.build();
    }

    @Override
    public TestEntitySkeleton createSkeleton(TestEntity entity) {
        return new TestEntitySkeleton();
    }

    @Override
    public Animator<TestEntity, TestEntitySkeleton> createAnimator(TestEntity entity, TestEntitySkeleton skeleton) {
        return null;
    }
}
