package com.unioncloud.newpay.presentation.ui.gift;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.esummer.android.FragmentStackDelegate;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.pax.baselib.card.TrackData;
import com.unioncloud.newpay.domain.model.erp.GiftCard;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.OriginalRefundInfo;
import com.unioncloud.newpay.presentation.presenter.gift.QueryGiftCardHandler;
import com.unioncloud.newpay.presentation.ui.QueryCardFragment;
import com.unioncloud.newpay.presentation.ui.pay.GiftCardFragment;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;
import com.unioncloud.newpay.presentation.ui.refund.GiftCardRefundFragment;

import java.util.List;

/**
 * Created by cwj on 16/9/3.
 */
public class QueryGiftCardFragment extends QueryCardFragment {

    public static QueryGiftCardFragment newRefund() {
        QueryGiftCardFragment fragment = new QueryGiftCardFragment();
        fragment.getArguments().putInt(KEY_GIFT_CARD_USE, GIFT_CARD_USE_REFUND);
        return fragment;
    }

    public static QueryGiftCardFragment newPay() {
        QueryGiftCardFragment fragment = new QueryGiftCardFragment();
        fragment.getArguments().putInt(KEY_GIFT_CARD_USE, GIFT_CARD_USE_PAY);
        return fragment;
    }

    private static final String KEY_GIFT_CARD_USE = "GIFT_CARD_USE";
    private static final int GIFT_CARD_USE_PAY = 0X0100;
    private static final int GIFT_CARD_USE_REFUND = 0X0200;

    private static StateUpdateHandlerListener<QueryGiftCardFragment, QueryGiftCardHandler> queryHandlerListener =
            new StateUpdateHandlerListener<QueryGiftCardFragment, QueryGiftCardHandler>() {
                @Override
                public void onUpdate(String key, QueryGiftCardFragment handler, QueryGiftCardHandler response) {
                    handler.dealQuery(response);
                }

                @Override
                public void onCleanup(String key, QueryGiftCardFragment handler, QueryGiftCardHandler response) {
                    response.removeCompletionListener(handler.queryListener);
                }
            };
    private UpdateCompleteCallback<QueryGiftCardHandler> queryListener =
            new UpdateCompleteCallback<QueryGiftCardHandler>() {
                @Override
                public void onCompleted(QueryGiftCardHandler response, boolean isSuccess) {
                    dealQuery(response);
                }
            };

    private FragmentStackDelegate stackDelegate;

    public OriginalRefundInfo getOriginalRefundInfo() {
        return getArguments().getParcelable("originalRefundInfo");
    }

    @Override
    protected String getTitle() {
        return "储值卡查询";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stackDelegate = (FragmentStackDelegate) getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tipsTv.setText("请刷储值卡");
    }

    @Override
    protected boolean isStrictMode() {
        return true;
    }

    @Override
    protected void queryCardByBillNo(String billNo) {
        // strict mode, do nothing.
    }

    @Override
    protected void queryCardByTrack(TrackData trackData) {
        QueryGiftCardHandler handler = new QueryGiftCardHandler(trackData.getTrack2(),
                PosDataManager.getInstance().getPosInfo());
        updateForResponse("QueryGiftCardFragment:query", handler, queryHandlerListener);
        handler.run();
    }

    private void dealQuery(QueryGiftCardHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("查询储值卡...");
                handler.addCompletionListener(queryListener);
            } else {
                if (handler.isSuccess()) {
                    usingGiftCard(handler.getData());
                } else {
                    showToast("查询储值卡失败");
                }
                cleanupResponse("QueryGiftCardFragment:query");
                dismissProgressDialog();
            }
        }
    }

    private void usingGiftCard(GiftCard giftCard) {
        int usingType = getArguments().getInt(KEY_GIFT_CARD_USE);
        switch (usingType) {
            case GIFT_CARD_USE_PAY:
                toGiftCardPay(giftCard);
                return;
            case GIFT_CARD_USE_REFUND:
                toGiftCardRefund(giftCard);
                return;
        }
    }

    private void toGiftCardRefund(GiftCard giftCard) {
        GiftCardRefundFragment fragment = GiftCardRefundFragment.newInstance(giftCard);
        fragment.getArguments().putParcelable("originalRefundInfo", getOriginalRefundInfo());
        if (stackDelegate != null) {
            stackDelegate.push(this, fragment);
        }
    }

    private void toGiftCardPay(GiftCard giftCard) {
        if (!verifySingle(giftCard)) {
            showToast("已使用了该储值卡,请更换卡");
            return;
        }
        GiftCardFragment fragment = GiftCardFragment.newInstance(giftCard);
        if (stackDelegate != null) {
            stackDelegate.push(this, fragment);
        }
    }

    private boolean verifySingle(GiftCard giftCard) {
        Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(PaymentSignpost.GIFT.numberToInt());
        List<PaymentUsed> paymentUseds = CheckoutDataManager.getInstance().getUsedPayments().getUsedList();
        for (PaymentUsed used : paymentUseds) {
            if (used.getPaymentId().equals(payment.getPaymentId())
                    && giftCard.getCardNumber().equals(used.getRelationNumber())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCancelQuery() {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stackDelegate = null;
    }
}
