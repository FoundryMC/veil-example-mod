package foundry.veil.example.client.render.entity.mogul;

import foundry.veil.api.client.necromancer.animation.Animator;
import foundry.veil.example.entity.MogulEntity;

public class MogulAnimator extends Animator<MogulEntity, MogulSkeleton> {
    public MogulAnimator(MogulEntity parent, MogulSkeleton skeleton) {
        super(parent, skeleton);
    }

    @Override
    public void animate() {
        super.animate();
    }
}

