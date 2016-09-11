package com.unioncloud.newpay.presentation.ui;

import com.esummer.android.fragment.StatedFragment;

/**
 * Created by cwj on 16/7/27.
 */
public class LoadingFragment extends StatedFragment implements LoadingDataView {

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        showToast(message);
    }
}
