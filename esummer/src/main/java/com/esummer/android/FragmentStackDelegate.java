package com.esummer.android;

import android.support.v4.app.Fragment;

import com.esummer.android.fragment.FragmentStack;

/**
 * Created by cwj on 16/7/6.
 */
public interface FragmentStackDelegate {

    boolean push(FragmentStack stack, Fragment fragment);

    boolean pop(FragmentStack stack, Fragment fragment);

}
