package com.unioncloud.newpay.presentation.ui.vip;

import android.os.Bundle;

import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.pax.baselib.card.TrackData;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.vip.QueryVipHandler;
import com.unioncloud.newpay.presentation.ui.QueryCardFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cwj on 16/8/14.
 */
public class QueryVipFragment extends QueryCardFragment {

    public static QueryVipFragment newInstance() {
        QueryVipFragment fragment = new QueryVipFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<QueryVipFragment, QueryVipHandler> handlerListener =
            new StateUpdateHandlerListener<QueryVipFragment, QueryVipHandler>() {
                @Override
                public void onUpdate(String key, QueryVipFragment handler, QueryVipHandler response) {
                    handler.dealQueryVip(response);
                }

                @Override
                public void onCleanup(String key, QueryVipFragment handler, QueryVipHandler response) {
                    response.removeCompletionListener(handler.queryVipListener);
                }
            };
    private UpdateCompleteCallback<QueryVipHandler> queryVipListener = new UpdateCompleteCallback<QueryVipHandler>() {
        @Override
        public void onCompleted(QueryVipHandler response, boolean isSuccess) {
            dealQueryVip(response);
        }
    };

    private QueryVipCallback queryVipCallback;

    // 是否正在查询会员卡
    private boolean isVipQuerying() {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        return ((loadingKeys != null) && loadingKeys.length != 0);
    }

    /** 将正在加载的过程状态移除 */
    private void removeLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        if (loadingKeys != null) {
            ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
            loadingKeyList.remove(loadingKey);
            getArguments().putStringArray("CartFragment:loadingItemsArray",
                    loadingKeyList.toArray(new String[loadingKeyList.size()]));
        }
    }

    /** 将正在加载的过程状态移除保存 */
    private void putLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        if (loadingKeys == null) {
            loadingKeys = new String[0];
        }
        ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
        if (!loadingKeyList.contains(loadingKey)) {
            loadingKeyList.add(loadingKey);
        }
        getArguments().putStringArray("CartFragment:loadingItemsArray",
                loadingKeyList.toArray(new String[loadingKeyList.size()]));
    }

    @Override
    protected boolean supportQr() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "会员卡查询";
    }

    @Override
    protected void onCancelQuery() {
        if (queryVipCallback != null) {
            queryVipCallback.onQueryCancel();
        }
    }

    @Override
    protected void queryCardByBillNo(String billNo) {
        if (!isVipQuerying()) {
            putLoadingItem("QueryVipFragment:VipQuerying");
            QueryVipHandler handler = new QueryVipHandler(null, billNo,
                    PosDataManager.getInstance().getPosInfo());
            updateForResponse("QueryVipFragment:VipQuerying", handler, handlerListener);
            handler.run();
        }
    }

    @Override
    protected void queryCardByTrack(TrackData trackData) {
        if (!isVipQuerying()) {
            putLoadingItem("QueryVipFragment:VipQuerying");
            QueryVipHandler handler = new QueryVipHandler(trackData.getTrack2(), null,
                    PosDataManager.getInstance().getPosInfo());
            updateForResponse("QueryVipFragment:VipQuerying", handler, handlerListener);
            handler.run();
        }
    }

    private void dealQueryVip(QueryVipHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在查询会员信息...");
                handler.addCompletionListener(queryVipListener);
            } else if (isVisible()) {
                removeLoadingItem("QueryVipFragment:VipQuerying");
                cleanupResponse("QueryVipFragment:VipQuerying");
                dismissProgressDialog();
                if (handler.isSuccess()) {
                    if (queryVipCallback != null) {
                        queryVipCallback.onQuerySuccess(handler.getData());
                    }
                } else {
                    showToast("查询会员卡失败");
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryVipCallback = (QueryVipCallback) getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        queryVipCallback = null;
    }

}
