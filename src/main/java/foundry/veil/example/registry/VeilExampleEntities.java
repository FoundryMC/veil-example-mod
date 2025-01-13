package foundry.veil.example.registry;

import foundry.veil.example.VeilExampleMod;
import foundry.veil.example.entity.TestEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;

public class VeilExampleEntities {

    public static final EntityType<TestEntity> TEST = register("test", EntityType.Builder.of(TestEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F)
            .clientTrackingRange(8));

    public static void bootstrap() {
    }

    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, VeilExampleMod.path(name), builder.build(null));
    }
}
