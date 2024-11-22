package foundry.veil.example;

import foundry.veil.example.registry.VeilExampleBlocks;
import foundry.veil.example.registry.VeilExampleItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeilExampleMod implements ModInitializer {

    public static final String MODID = "veil-example-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    private static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, VeilExampleMod.path("items"));

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @Override
    public void onInitialize() {
        VeilExampleItems.bootstrap();
        VeilExampleBlocks.bootstrap();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP, FabricItemGroup.builder().title(Component.translatable(MODID + ".items")).icon(() -> new ItemStack(VeilExampleBlocks.MAP)).displayItems(VeilExampleItems::fillTab).build());
    }
}