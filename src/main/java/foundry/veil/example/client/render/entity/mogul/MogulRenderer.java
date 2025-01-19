package foundry.veil.example.client.render.entity.mogul;

import foundry.veil.api.client.necromancer.animation.Animator;
import foundry.veil.api.client.necromancer.render.NecromancerEntityRenderer;
import foundry.veil.api.client.necromancer.render.NecromancerSkinEntityRenderLayer;
import foundry.veil.api.client.necromancer.render.Skin;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.example.VeilExampleMod;
import foundry.veil.example.entity.MogulEntity;
import foundry.veil.example.entity.TestEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MogulRenderer extends NecromancerEntityRenderer<MogulEntity, MogulSkeleton> {
    private static final ResourceLocation RENDERTYPE = VeilExampleMod.path("mogul_entity");
    private static final ResourceLocation TEXTURE_LOCATION = VeilExampleMod.path("textures/entity/mogul.png");

    public MogulRenderer(EntityRendererProvider.Context context) {
        super(context, 1.5F);
        this.addLayer(new NecromancerSkinEntityRenderLayer<>(this) {
            @Override
            public RenderType getRenderType(MogulEntity entity) {
                return VeilRenderType.get(RENDERTYPE, TEXTURE_LOCATION);
            }
            @Override
            public Skin getSkin(MogulEntity parent) {
                return MogulSkin.MOGUL_SKIN;
            }
        });
    }

    @Override
    public MogulSkeleton createSkeleton(MogulEntity gnomadMogulEntity) {
        return new MogulSkeleton();
    }

    @Override
    public Animator<MogulEntity, MogulSkeleton> createAnimator(MogulEntity gnomadMogulEntity, MogulSkeleton mogulSkeleton) {
        return new MogulAnimator(gnomadMogulEntity, mogulSkeleton);
    }
}
