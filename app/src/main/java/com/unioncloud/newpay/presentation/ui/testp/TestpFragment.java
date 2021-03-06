package com.unioncloud.newpay.presentation.ui.testp;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.testp.Testp;
import com.unioncloud.newpay.presentation.presenter.testp.QueryTestpHandler;

public class TestpFragment extends StatedFragment {

    private TestpBinder testpBinder;

    public TestpFragment() {
        // Required empty public constructor
    }

    public static TestpFragment newInstance() {
        TestpFragment fragment = new TestpFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testp, container, false);
        testpBinder = new TestpBinder();
        return view;
    }

    private QueryTestpHandler createHandler() {
        Testp testp = new Testp();
        testp.setCkcode("0001");
        testp.setMkcode("0002");
        QueryTestpHandler queryTestpHandler = new QueryTestpHandler(testp, true);
        new Thread(queryTestpHandler).start();
        return queryTestpHandler;
    }

    private void toTest() {
        updateForResponse("KEY_TESTP", createHandler(), queryTestpListener);
    }

    private void dealTest(QueryTestpHandler response) {
        if (response == null) {
            return;
        }
        if (response.isUpdating()) {
            Log.i("----thread3---main:", Looper.getMainLooper().getThread().toString());
            Log.i("----thread3---now",Thread.currentThread().toString());
            showProgressDialog("正在执行中...");
            response.addCompletionListener(testpListener);
//            dismissProgressDialog();
        }else{
            if (response.isSuccess()) {
                dismissProgressDialog();
//                在主线程更新UI
                ThreadingUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        String ak48 = testpBinder.et_ckcode_input.getText().toString();
                        testpBinder.tv_ckcode.setText(ak48);
                        String ak49 =testpBinder.et_type_input.getText().toString();
                        testpBinder.tv_custAddr.setText(ak49);
                    }
                });

            }else{
                showToast("Test失败:" );
                dismissProgressDialog();

            }
            Log.i("----***----","***");
            cleanupResponse("KEY_TESTP");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        testpBinder.onBind(view);
        testpBinder.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest();
            }
        });
    }

    private StateUpdateHandlerListener<TestpFragment, QueryTestpHandler> queryTestpListener
            = new StateUpdateHandlerListener<TestpFragment, QueryTestpHandler>() {
        //        该方法被初始化绑定会调用一次
        @Override
        public void onUpdate(String key, TestpFragment handler, QueryTestpHandler response) {
            dealTest(response);
        }

        @Override
        public void onCleanup(String key, TestpFragment handler, QueryTestpHandler response) {
            Log.i("----666----","666");
        }
    };


    private UpdateCompleteCallback<QueryTestpHandler> testpListener = new UpdateCompleteCallback<QueryTestpHandler>() {
        @Override
        public void onCompleted(QueryTestpHandler response, boolean isSuccess) {
            dealTest(response);
        };
    };

}

