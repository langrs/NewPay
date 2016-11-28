package com.unioncloud.newpay.presentation.ui.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.esummer.android.utils.DeviceUtils;
import com.unioncloud.newpay.domain.model.pos.PosRegister;
import com.unioncloud.newpay.presentation.presenter.register.RegisterPosHandler;

/**
 * Created by cwj on 16/7/26.
 */
public class RegisterFragment extends StatedFragment {

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();

        return fragment;
    }

    private static StateUpdateHandlerListener<RegisterFragment, RegisterPosHandler> handlerListener =
            new StateUpdateHandlerListener<RegisterFragment, RegisterPosHandler>() {
                @Override
                public void onUpdate(String key, RegisterFragment handler, RegisterPosHandler response) {
                    handler.dealRegister(response);
                }

                @Override
                public void onCleanup(String key, RegisterFragment handler, RegisterPosHandler response) {
                    response.removeCompletionListener(handler.registerListener);
                }
            };

    private UpdateCompleteCallback<RegisterPosHandler> registerListener = new UpdateCompleteCallback<RegisterPosHandler>() {
        @Override
        public void onCompleted(RegisterPosHandler response, boolean isSuccess) {
            dealRegister(response);
        }
    };
    private RegisterViewBinder viewBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinder = new RegisterViewBinder();
        return inflater.inflate(viewBinder.getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("POS机注册");
        viewBinder.bindView(view);
        viewBinder.posTypeSpinner.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[]{"商铺收银", "自营收银"}));

        viewBinder.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        PosRegister registerInfo = getRegisterInfo(viewBinder);
        if (registerInfo != null) {
            updateForResponse("KEY_POS_REGISTER", getRegisterHandler(registerInfo), handlerListener);
        }
    }

    private void dealRegister(RegisterPosHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
//                handler.callOrSubscribe(registerListener);
                handler.addCompletionListener(registerListener);
            } else {
                if (handler.isSuccess() && handler.getData().isSuccess()) {
                    showRegisterResult("POS信息注册成功");
                    getActivity().finish();
                } else {
                    showRegisterResult("POS信息注册失败" + handler.getData().getErrorMessage());
                }
                dismissProgressDialog();
                cleanupResponse("KEY_POS_REGISTER");
            }
        }
    }

    private void showRegisterResult(final String message) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                showToast(message);
            }
        });
    }

    private RegisterPosHandler getRegisterHandler(PosRegister registerInfo) {
        RegisterPosHandler handler = new RegisterPosHandler(true, registerInfo);
        handler.run();
        return handler;
    }

    private PosRegister getRegisterInfo(RegisterViewBinder viewBinder) {
//        String shopNo = viewBinder.shopNoEt.getText().toString();
//        if (TextUtils.isEmpty(shopNo)){
//            viewBinder.shopNoEt.setError("分店不能为空");
//            return null;
//        }
        String storeNo = viewBinder.storeNoEt.getText().toString();
        if (TextUtils.isEmpty(storeNo)) {
            viewBinder.storeNoEt.setError("店铺不能为空");
            return null;
        }
//        String userNo = viewBinder.userNoEt.getText().toString();
//        if (TextUtils.isEmpty(userNo)) {
//            viewBinder.userNoEt.setError("操作人员不能为空");
//            return null;
//        }
        String posNo = viewBinder.posNoEt.getText().toString();
        if (TextUtils.isEmpty(posNo)) {
            viewBinder.posNoEt.setError("POS编号不能为空");
            return null;
        }
        int selectPosition = viewBinder.posTypeSpinner.getSelectedItemPosition();
        String posType = (selectPosition == 0) ? "01" :"02";
        PosRegister registerInfo = new PosRegister();
//        registerInfo.setShopNo(shopNo);
        registerInfo.setStoreNo(storeNo);
//        registerInfo.setCashierNo(userNo);
        registerInfo.setPosNo(posNo);
        registerInfo.setPosType(posType);
        registerInfo.setMac(DeviceUtils.getWifiMac(getContext()));
        return registerInfo;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
