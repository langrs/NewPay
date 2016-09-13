package com.unioncloud.newpay.presentation.ui.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.coupon.CreatePointsCouponHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintCouponHandler;

/**
 * Created by cwj on 16/9/13.
 */
public class PointRebateDetailFragment extends StatedFragment {

    public static PointRebateDetailFragment newInstance(VipPointsRebate rebate) {
        PointRebateDetailFragment fragment = new PointRebateDetailFragment();
        fragment.setPointRebate(rebate);
        return fragment;
    }

    private static StateUpdateHandlerListener<PointRebateDetailFragment, CreatePointsCouponHandler> createHandlerListener =
            new StateUpdateHandlerListener<PointRebateDetailFragment, CreatePointsCouponHandler>() {
                @Override
                public void onUpdate(String key, PointRebateDetailFragment handler, CreatePointsCouponHandler response) {
                    handler.dealCreateCoupon(response);
                }

                @Override
                public void onCleanup(String key, PointRebateDetailFragment handler, CreatePointsCouponHandler response) {
                    response.removeCompletionListener(handler.createListener);
                }
            };
    private UpdateCompleteCallback<CreatePointsCouponHandler> createListener =
            new UpdateCompleteCallback<CreatePointsCouponHandler>() {
                @Override
                public void onCompleted(CreatePointsCouponHandler response, boolean isSuccess) {
                    dealCreateCoupon(response);
                }
            };

    private static StateUpdateHandlerListener<PointRebateDetailFragment, PrintCouponHandler> printHandlerListener =
            new StateUpdateHandlerListener<PointRebateDetailFragment, PrintCouponHandler>() {
                @Override
                public void onUpdate(String key, PointRebateDetailFragment handler, PrintCouponHandler response) {
                    handler.dealPrint(response);
                }

                @Override
                public void onCleanup(String key, PointRebateDetailFragment handler, PrintCouponHandler response) {
                    response.removeCompletionListener(handler.printListener);
                }
            };
    private UpdateCompleteCallback<PrintCouponHandler> printListener =
            new UpdateCompleteCallback<PrintCouponHandler>() {
                @Override
                public void onCompleted(PrintCouponHandler response, boolean isSuccess) {
                    dealPrint(response);
                }
            };

    private void setPointRebate(VipPointsRebate rebate) {
        getArguments().putSerializable("pointsRebate", rebate);
    }

    private VipPointsRebate getPointRebate() {
        return (VipPointsRebate) getArguments().getSerializable("pointsRebate");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_point_rebate_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView vipNo = (TextView) view.findViewById(R.id.fragment_point_rebate_vip_no);
        TextView phoneNo = (TextView) view.findViewById(R.id.fragment_point_rebate_vip_phone);
        TextView vipName = (TextView) view.findViewById(R.id.fragment_point_rebate_vip_name);
        TextView originalPoints = (TextView) view.findViewById(R.id.fragment_point_rebate_original_point);
        TextView usePoint = (TextView) view.findViewById(R.id.fragment_point_rebate_use_point);
        TextView amount = (TextView) view.findViewById(R.id.fragment_point_rebate_amount);
        Button cancelBtn = (Button) view.findViewById(R.id.fragment_point_rebate_cancel_btn);
        Button okBtn = (Button) view.findViewById(R.id.fragment_point_rebate_ok_btn);

        VipPointsRebate rebate = getPointRebate();
        vipNo.setText(rebate.getCardNumber());
        phoneNo.setText(rebate.getPhoneNumber());
        vipName.setText(rebate.getName());
        originalPoints.setText(rebate.getPoints());
        usePoint.setText(rebate.getRebatePoints());
        amount.setText(rebate.getRebateAmount());
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCreateCoupon();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCoupon();
            }
        });
    }

    private void cancelCreateCoupon() {
        getActivity().finish();
    }

    private void createCoupon() {
        CreatePointsCouponHandler handler = new CreatePointsCouponHandler(getPointRebate(),
                PosDataManager.getInstance().getPosInfo().getUserNumber());
        updateForResponse("PointRebateDetailFragment:createCoupon", handler, createHandlerListener);
        handler.run();
    }

    private void dealCreateCoupon(CreatePointsCouponHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在兑换积分返利券");
                handler.addCompletionListener(createListener);
            } else {
                cleanupResponse("PointRebateDetailFragment:createCoupon");
                if (handler.isSuccess()) {
                    printCoupon(handler.getData());
                } else {
                    showToast("兑券失败");
                    dismissProgressDialog();
                }
            }
        }
    }

    private void printCoupon(Coupon coupon) {
        PrintCouponHandler handler = new PrintCouponHandler(getActivity(), coupon);
        updateForResponse("PointRebateDetailFragment:printCoupon", handler, printHandlerListener);
        handler.run();
    }

    private void dealPrint(PrintCouponHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在打印折扣券...");
                handler.addCompletionListener(printListener);
            } else {
                dismissProgressDialog();
                cleanupResponse("PointRebateDetailFragment:printCoupon");
                if (!handler.isSuccess() || !handler.getData()) {
                    showToast("打印失败");
                }
                onPrintFinish();
            }
        }
    }

    private void onPrintFinish() {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
