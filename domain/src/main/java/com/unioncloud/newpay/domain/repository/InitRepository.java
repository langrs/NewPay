package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.login.User;
import com.unioncloud.newpay.domain.model.param.Code;
import com.unioncloud.newpay.domain.model.param.ExtraParam;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentMode;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

/**
 * Created by cwj on 16/7/1.
 */
public interface InitRepository {

    boolean saveUser(User user);

    boolean savePos(PosInfo posInfo);

    boolean savePaymentModes(List<PaymentMode> list);

    boolean savePayments(List<Payment> list);

    boolean saveExtraParams(List<ExtraParam> list);

    boolean saveCodes(List<Code> list);
}
