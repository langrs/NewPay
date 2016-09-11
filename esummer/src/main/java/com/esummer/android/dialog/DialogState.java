package com.esummer.android.dialog;

import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/4/26.
 */
class DialogState {
    public int dialogId;
    public UniversalDialog dialog;
    public Delegate<UniversalDialog> positiveListener;
    public Delegate<UniversalDialog> neutralListener;
    public Delegate<UniversalDialog> negativeListener;
    public Delegate<UniversalDialog> dismissListener;
}
