package com.esummer.android.utils;

import com.esummer.android.R;

/**
 * Created by cwj on 16/4/6.
 */
public class TransitionAnimationsUtil {

    public static final TransitionAnimations NONE_TRANSITION =
            new TransitionAnimations(0, 0);

    public static final TransitionAnimations RIGHT_TRANSITION =
            new TransitionAnimations(R.anim.in_from_right, R.anim.out_to_left);

    public static final TransitionAnimations LEFT_TRANSITION =
            new TransitionAnimations(R.anim.in_from_left, R.anim.out_to_right);

    public static final TransitionAnimations FADE_TRANSITION =
            new TransitionAnimations(R.anim.fade_in, R.anim.fade_out);
}
