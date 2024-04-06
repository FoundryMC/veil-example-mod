package foundry.veil.example.registry;

import foundry.veil.example.VeilExampleMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class VeilExampleItems {

    public static final Set<Item> ITEMS = new HashSet<>();

    public static void bootstrap() {
    }

    public static <T extends Item> T register(String name, T item) {
        T value = Registry.register(BuiltInRegistries.ITEM, VeilExampleMod.path(name), item);
        ITEMS.add(value);
        return value;
    }

    public static void fillTab(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output output) {
        ITEMS.stream().sorted(Comparator.comparing(BuiltInRegistries.ITEM::getKey)).forEach(item -> output.accept(new ItemStack(item)));
    }
}
