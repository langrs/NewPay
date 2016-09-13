package com.unioncloud.newpay.presentation.ui.vip;

import com.unioncloud.newpay.domain.model.erp.VipCard;

/**
 * Created by cwj on 16/9/13.
 */
public interface QueryVipCallback {

    void onQuerySuccess(VipCard vipCard);

    void onQueryCancel();
}
