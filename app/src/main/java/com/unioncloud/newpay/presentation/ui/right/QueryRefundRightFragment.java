package com.unioncloud.newpay.presentation.ui.right;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.presenter.right.QueryRefundRightHandler;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryRefundRightFragment extends StatedFragment {

    public static QueryRefundRightFragment newInstance() {
        QueryRefundRightFragment fragment = new QueryRefundRightFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<QueryRefundRightFragment, QueryRefundRightHandler> queryHandlerListener =
            new StateUpdateHandlerListener<QueryRefundRightFragment, QueryRefundRightHandler>() {
                @Override
                public void onUpdate(String key, QueryRefundRightFragment handler, QueryRefundRightHandler response) {
                    handler.dealQuery(response);
                }

                @Override
                public void onCleanup(String key, QueryRefundRightFragment handler, QueryRefundRightHandler response) {
                    response.removeCompletionListener(handler.queryListener);
                }
            };
    private UpdateCompleteCallback<QueryRefundRightHandler> queryListener =
            new UpdateCompleteCallback<QueryRefundRightHandler>() {
                @Override
                public void onCompleted(QueryRefundRightHandler response, boolean isSuccess) {
                    dealQuery(response);
                }
            };

    protected EditText numberEt;
    protected EditText passwordEt;
    protected Button cancelButton;
    protected Button okButton;

    private RightListener rightListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rightListener = (RightListener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_refund_right, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numberEt = (EditText) view.findViewById(R.id.fragment_query_right_no);
        passwordEt = (EditText) view.findViewById(R.id.fragment_query_right_password);
        cancelButton = (Button) view.findViewById(R.id.fragment_query_right_cancel_btn);
        okButton = (Button) view.findViewById(R.id.fragment_query_right_ok_btn);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryRefundRight();
            }
        });

    }

    private void onCancel() {
        getActivity().finish();
    }

    private void queryRefundRight() {
        String number = numberEt.getText().toString();
        if (TextUtils.isEmpty(number)) {
            showToast("权限号不能为空!");
            return;
        }
        String password = passwordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码!");
            return;
        }
//        先把授权人写入,如果校验不成功,再清空
        RefundDataManager.getInstance().setAuthorizer(number);
        QueryRefundRightHandler handler = new QueryRefundRightHandler(number, password);
        updateForResponse("QueryRefundRightFragment:queryRefundRight", handler, queryHandlerListener);
        handler.run();
    }

    private void dealQuery(QueryRefundRightHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在查询权限...");
                handler.addCompletionListener(queryListener);
            } else {
                if (rightListener != null) {
                    rightListener.onQueryFinish(handler.isSuccess() && handler.getData());
                }
                cleanupResponse("QueryRefundRightFragment:queryRefundRight");
                dismissProgressDialog();
            }
        }
    }

    @Override
    public void onDestroyView() {
        rightListener = null;
        super.onDestroyView();
    }

}
