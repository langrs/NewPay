package com.unioncloud.newpay.presentation.ui.coupon;

import android.os.Bundle;

import com.esummer.android.FragmentStackDelegate;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.pax.baselib.card.TrackData;
import com.unioncloud.newpay.domain.model.erp.QueryCardCommand;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.coupon.QueryPointsRebateHandler;
import com.unioncloud.newpay.presentation.ui.QueryCardFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryPointsRebateFragment extends QueryCardFragment {

    public static QueryPointsRebateFragment newInstance() {
        QueryPointsRebateFragment fragment = new QueryPointsRebateFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<QueryPointsRebateFragment, QueryPointsRebateHandler> queryHandlerListener
            = new StateUpdateHandlerListener<QueryPointsRebateFragment, QueryPointsRebateHandler>() {
        @Override
        public void onUpdate(String key, QueryPointsRebateFragment handler, QueryPointsRebateHandler response) {
            handler.dealQuery(response);
        }

        @Override
        public void onCleanup(String key, QueryPointsRebateFragment handler, QueryPointsRebateHandler response) {
            response.removeCompletionListener(handler.queryListener);
        }
    };
    private UpdateCompleteCallback<QueryPointsRebateHandler> queryListener =
            new UpdateCompleteCallback<QueryPointsRebateHandler>() {
                @Override
                public void onCompleted(QueryPointsRebateHandler response, boolean isSuccess) {
                    dealQuery(response);
                }
            };

    private FragmentStackDelegate stackDelegate;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stackDelegate = (FragmentStackDelegate) getActivity();
    }

    @Override
    protected void onCancelQuery() {
        getActivity().finish();
    }

    @Override
    protected String getTitle() {
        return "积分返利查询";
    }

    private String getShopId() {
        return PosDataManager.getInstance().getPosInfo().getShopId();
    }

    @Override
    protected void queryCardByBillNo(String billNo) {
        if (!isQuerying()) {
            putLoadingItem("QueryPointsRebateFragment:querying");
            QueryCardCommand command = new QueryCardCommand();
            command.setBillNo(billNo);
            command.setShopId(getShopId());
            queryPointsRebate(command);
        }
    }

    @Override
    protected void queryCardByTrack(TrackData trackData) {
        if (!isQuerying()) {
            putLoadingItem("QueryPointsRebateFragment:querying");
            QueryCardCommand command = new QueryCardCommand();
            command.setTrackInfo(trackData.getTrack2());
            command.setShopId(getShopId());
            queryPointsRebate(command);
        }
    }

    // 是否正在查询会员卡
    private boolean isQuerying() {
        String[] loadingKeys = getArguments().getStringArray("QueryPointsRebateFragment:loadingItemsArray");
        return ((loadingKeys != null) && loadingKeys.length != 0);
    }

    private void queryPointsRebate(QueryCardCommand command) {
        QueryPointsRebateHandler handler = new QueryPointsRebateHandler(command);
        updateForResponse("queryPointsRebate", handler, queryHandlerListener);
        handler.run();
    }

    private void dealQuery(QueryPointsRebateHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在查询积分返利...");
                handler.addCompletionListener(queryListener);
            } else {
                if (handler.isSuccess()) {
                    onQuerySuccess(handler.getData());
                }
                removeLoadingItem("QueryPointsRebateFragment:querying");
                dismissProgressDialog();
                cleanupResponse("queryPointsRebate");
            }
        }
    }

    private void onQuerySuccess(VipPointsRebate rebate) {
        PointRebateDetailFragment fragment = PointRebateDetailFragment.newInstance(rebate);
        if (stackDelegate != null) {
            stackDelegate.push(this, fragment);
        }
    }

    /** 将正在加载的过程状态移除 */
    private void removeLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        if (loadingKeys != null) {
            ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
            loadingKeyList.remove(loadingKey);
            getArguments().putStringArray("QueryPointsRebateFragment:loadingItemsArray",
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
        getArguments().putStringArray("QueryPointsRebateFragment:loadingItemsArray",
                loadingKeyList.toArray(new String[loadingKeyList.size()]));
    }

    @Override
    public void onDestroyView() {
        stackDelegate = null;
        super.onDestroyView();
    }
}
