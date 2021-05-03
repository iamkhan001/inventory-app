package com.nymbleup.inventory.views.indicator.animation;

import androidx.annotation.NonNull;

import com.nymbleup.inventory.views.indicator.animation.controller.AnimationController;
import com.nymbleup.inventory.views.indicator.animation.controller.ValueController;
import com.nymbleup.inventory.views.indicator.draw.data.Indicator;

public class AnimationManager {

    private AnimationController animationController;

    public AnimationManager(@NonNull Indicator indicator, @NonNull ValueController.UpdateListener listener) {
        this.animationController = new AnimationController(indicator, listener);
    }

    public void basic() {
        if (animationController != null) {
            animationController.end();
            animationController.basic();
        }
    }

    public void interactive(float progress) {
        if (animationController != null) {
            animationController.interactive(progress);
        }
    }

    public void end() {
        if (animationController != null) {
            animationController.end();
        }
    }
}
