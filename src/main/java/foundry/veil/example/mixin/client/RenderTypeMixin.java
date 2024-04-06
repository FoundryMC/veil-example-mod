package foundry.veil.example.mixin.client;

import foundry.veil.example.registry.VeilExampleRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @Inject(method = "entityCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;", at = @At("HEAD"), cancellable = true)
    private static void replaceEntity(ResourceLocation resourceLocation, boolean bl, CallbackInfoReturnable<RenderType> cir) {
        cir.setReturnValue(VeilExampleRenderTypes.cursedTessellationRenderTypeEntityCutoutNoCullWhyIsThisInAFundyVideoIBetHeWillProbablySayHeIsTheOneWhoCodedItInMinecraft(resourceLocation, bl));
    }
}
