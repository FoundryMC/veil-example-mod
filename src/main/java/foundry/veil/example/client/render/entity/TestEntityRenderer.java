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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TestEntityRenderer extends NecromancerEntityRenderer<TestEntity, TestEntitySkeleton> {

    private static final TestEntitySkeleton SKELETON = new TestEntitySkeleton();
    private static final Skin TEST_SKIN = createSkin();
    private static final ResourceLocation RENDERTYPE = VeilExampleMod.path("test_entity");
    private static final ResourceLocation TEXTURE_LOCATION = VeilExampleMod.path("textures/entity/test.png");

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
    }

    @Override
    public void render(TestEntity testEntity, NecromancerRenderer context, MatrixStack matrixStack, float partialTicks) {
        for (Bone bone : SKELETON.bones.values()) {
            bone.reset();
            bone.color.set(1, 0, 1, 1);
            bone.x = -4;
        }
        super.render(testEntity, context, matrixStack, 1.0F);
    }

    private static Skin createSkin() {
        Skin.Builder builder = Skin.builder(64, 64);

        builder.startBone("test");
        builder.addCube(4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, false);
        builder.endBone();

        return builder.build();
    }

    @Override
    public TestEntitySkeleton createSkeleton(TestEntity entity) {
        return SKELETON;
    }

    @Override
    public Animator<TestEntity, TestEntitySkeleton> createAnimator(TestEntity entity, TestEntitySkeleton skeleton) {
        return null;
    }
}
