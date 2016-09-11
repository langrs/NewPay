package com.unioncloud.newpay.data.repository.login;

import com.unioncloud.newpay.data.entity.login.LoginResultEntity;
import com.unioncloud.newpay.data.entity.param.CodeEntity;
import com.unioncloud.newpay.data.entity.param.PosExtraParmEntity;
import com.unioncloud.newpay.data.entity.paymethod.PayMethodEntity;
import com.unioncloud.newpay.data.entity.paymethod.PayMethodGroupEntity;
import com.unioncloud.newpay.data.entity.pos.PosEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.login.LoginResult;
import com.unioncloud.newpay.domain.model.login.UserLogin;
import com.unioncloud.newpay.domain.model.param.Code;
import com.unioncloud.newpay.domain.model.param.ExtraParam;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentMode;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.LoginRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/8.
 */
public class LoginDataRepository implements LoginRepository {

    @Override
    public Observable<LoginResult> login(UserLogin userLogin) {
        return StoreFactory.getLoginStore().login(userLogin.getUserNumber(),
                userLogin.getUserPassword(), userLogin.getMac())
                .map(new Func1<LoginResultEntity, LoginResult>() {
                    @Override
                    public LoginResult call(LoginResultEntity entity) {
                        return mapper(entity);
                    }
                });
    }

    private static LoginResult mapper(LoginResultEntity entity) {
        LoginResult loginResult = new LoginResult();
        if (entity != null) {
            loginResult.setPosInfo(mapper(entity.getPos()));
            loginResult.setPaymentList(mapperPaymentList(entity.getPayMethodEntityList()));
            loginResult.setPaymentModeList(mapperPaymentModeList(entity.getPayMethodGroupList()));
            loginResult.setExtraParamList(mapperParmList(entity.getExtraParmList()));
            loginResult.setCodes(mapperCodeList(entity.getCodeList()));
        }
        return loginResult;
    }

    private static PosInfo mapper(PosEntity entity) {
        if (entity != null) {
            PosInfo posInfo = new PosInfo();
            posInfo.setCompanyId(entity.getCompanyId());
            posInfo.setShopId(entity.getShopId());
            posInfo.setShopNumber(entity.getShopNo());
            posInfo.setShopName(entity.getShopName());

            posInfo.setStoreId(entity.getStoreId());
            posInfo.setStoreNumber(entity.getStoreNo());
            posInfo.setStoreName(entity.getStoreName());

            posInfo.setPosId(entity.getPosId());
            posInfo.setPosType(entity.getPosType());
            posInfo.setPosNumber(entity.getPosNo());
            posInfo.setPosName(entity.getPosName());

            posInfo.setUserId(entity.getUserId());
            posInfo.setUserNumber(entity.getUserNo());
            posInfo.setUserName(entity.getUserName());

            posInfo.setServerAddress(entity.getServerAddr());
            posInfo.setPrintHeader(entity.getHead1());
            posInfo.setPrintFooter(entity.getFooter1());
            return posInfo;
        }
        return null;
    }

    public static ExtraParam mapper(PosExtraParmEntity entity) {
        if (entity != null) {
            ExtraParam param = new ExtraParam();
            param.setParamId(entity.getParmId());
            param.setParamNumber(entity.getParmNo());
            param.setParamName(entity.getParmName());
            param.setParamValue(entity.getParmVal());
            return param;
        }
        return null;
    }

    public static List<ExtraParam> mapperParmList(List<PosExtraParmEntity> entityList) {
        List<ExtraParam> list = new ArrayList<>();
        if (entityList == null) {
            return list;
        }
        for (PosExtraParmEntity entity : entityList) {
            list.add(mapper(entity));
        }
        return list;
    }

    public static Payment mapper(PayMethodEntity entity) {
        if (entity != null) {
            Payment payment = new Payment();
            payment.setPaymentId(entity.getPaymodeId());
            payment.setPaymentNumber(entity.getPaymodeNo());
            payment.setPaymentQy(entity.getPaymodeQy());
            payment.setPaymentName(entity.getPaymodeName());
            payment.setIsChange(entity.getIsChange());
            payment.setIsPoint(entity.getIsPoint());
            payment.setIsInvoice(entity.getIsInvoice());
            payment.setIsCoupon(entity.getIsCoupon());
            payment.setIsRefund(entity.getIsRet());
            payment.setIsFlag(entity.getIsFlag());
            payment.setCurrencyId(entity.getCurrencyId());
            payment.setIsLocalCurrency(entity.getIsLocalCurrency());
            payment.setExchangeRate(entity.getExchangeRate());
            return payment;
        }
        return null;
    }

    public static List<Payment> mapperPaymentList(List<PayMethodEntity> entityList) {
        List<Payment> list = new ArrayList<>();
        if (entityList == null) {
            return list;
        }
        for (PayMethodEntity entity : entityList) {
            list.add(mapper(entity));
        }
        return list;
    }

    public static PaymentMode mapper(PayMethodGroupEntity entity) {
        if (entity != null) {
            PaymentMode mode = new PaymentMode();
            mode.setParentId(entity.getParentId());
            mode.setPaymentModeId(entity.getPayviewId());
            mode.setPaymentModeName(entity.getPayviewName());
            mode.setPaymentModeNumber(entity.getPayviewNo());
            mode.setPaymentId(entity.getPaymodeId());
            return mode;
        }
        return null;
    }

    public static List<PaymentMode> mapperPaymentModeList(List<PayMethodGroupEntity> entityList) {
        List<PaymentMode> list = new ArrayList<>();
        if (entityList == null) {
            return list;
        }
        for (PayMethodGroupEntity entity : entityList) {
            list.add(mapper(entity));
        }
        return list;
    }

    public static Code mapper(CodeEntity entity) {
        if (entity != null) {
            Code code = new Code();
            code.setCodeType(entity.getCodeType());
            code.setCodeNumber(entity.getCodeNo());
            code.setCodeName(entity.getCodeName());
            return code;
        }
        return null;
    }

    public static List<Code> mapperCodeList(List<CodeEntity> entityList) {
        List<Code> list = new ArrayList<>();
        if (entityList == null) {
            return list;
        }
        for (CodeEntity entity : entityList) {
            list.add(mapper(entity));
        }
        return list;
    }

    @Override
    public Observable<Boolean> changePassword(String shopId, String userId, String newPassword,
                                              String oldPassword) {
        return StoreFactory.getLoginStore().changePassword(shopId, userId, newPassword, oldPassword);
    }
}
