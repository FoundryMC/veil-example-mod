package foundry.veil.example.client.render.entity;

import foundry.veil.api.client.necromancer.Bone;
import foundry.veil.api.client.necromancer.Skeleton;

public class TestEntitySkeleton extends Skeleton {

    public TestEntitySkeleton() {
        this.addBone(new Bone("test"));
        this.buildRoots();
    }
}
