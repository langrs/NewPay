package com.esummer.android.utils;

/**
 * Created by cwj on 16/4/6.
 */
public class TransitionAnimations {

    protected final int enterAnimation;
    protected final int exitAnimation;

    public TransitionAnimations(int enterAnimation, int exitAnimation) {
        this.enterAnimation = enterAnimation;
        this.exitAnimation = exitAnimation;
    }

    public int enterAnimation() {
        return enterAnimation;
    }

    public int exitAnimation() {
        return exitAnimation;
    }
}
