package com.unioncloud.newpay.data.repository;

import android.content.Context;

import com.unioncloud.newpay.data.repository.appupgrade.datasource.AppStore;
import com.unioncloud.newpay.data.repository.appupgrade.datasource.CloudAppStore;
import com.unioncloud.newpay.data.repository.bankcardpay.datasource.BankcardPayStore;
import com.unioncloud.newpay.data.repository.bankcardpay.datasource.PaxBankcardPayStore;
import com.unioncloud.newpay.data.repository.cart.datasource.CartStore;
import com.unioncloud.newpay.data.repository.cart.datasource.CloudCartStore;
import com.unioncloud.newpay.data.repository.checkout.datasource.CheckoutStore;
import com.unioncloud.newpay.data.repository.checkout.datasource.CloudCheckoutStore;
import com.unioncloud.newpay.data.repository.coupon.datasource.CloudCouponStore;
import com.unioncloud.newpay.data.repository.coupon.datasource.CouponStore;
import com.unioncloud.newpay.data.repository.gift.datasource.CloudGiftStore;
import com.unioncloud.newpay.data.repository.gift.datasource.GiftStore;
import com.unioncloud.newpay.data.repository.login.datasource.CloudLoginStore;
import com.unioncloud.newpay.data.repository.login.datasource.LoginStore;
import com.unioncloud.newpay.data.repository.notice.datasource.CloudNoticeStore;
import com.unioncloud.newpay.data.repository.notice.datasource.NoticeStore;
import com.unioncloud.newpay.data.repository.order.datasource.CloudOrderStore;
import com.unioncloud.newpay.data.repository.order.datasource.OrderStore;
import com.unioncloud.newpay.data.repository.pos.datasource.CloudPosStore;
import com.unioncloud.newpay.data.repository.pos.datasource.PosStore;
import com.unioncloud.newpay.data.repository.print.datasource.PaxPrintStore;
import com.unioncloud.newpay.data.repository.print.datasource.PrintStore;
import com.unioncloud.newpay.data.repository.product.datasource.CloudProductStore;
import com.unioncloud.newpay.data.repository.product.datasource.ProductStore;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.SwiftPassPayStore;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.ThirdPartyStore;
import com.unioncloud.newpay.data.repository.vip.datasource.VipDataStore;
import com.unioncloud.newpay.data.repository.vip.datasource.VipStore;

/**
 * Created by cwj on 16/8/8.
 */
public class StoreFactory {

    public static AppStore getAppStore() {
        return new CloudAppStore();
    }

    public static PosStore getPosStore() {
        return CloudPosStore.getInstance();
    }

    public static LoginStore getLoginStore() {
        return new CloudLoginStore();
    }

    public static CartStore getCartStore() {
        return new CloudCartStore();
    }

    public static ProductStore getProductStore() {
        return new CloudProductStore();
    }

    public static CheckoutStore getCheckoutStore() {
        return new CloudCheckoutStore();
    }

    public static OrderStore getOrderStore() {
        return new CloudOrderStore();
    }

    public static VipStore getVipStore() {
        return new VipDataStore();
    }

    public static BankcardPayStore getBankcardPayStore(Context context) {
        return new PaxBankcardPayStore(context);
    }

    public static ThirdPartyStore getThirdPartyStore() {
        return new SwiftPassPayStore();
    }

    public static PrintStore getPrintStore(Context context) {
        return new PaxPrintStore(context);
    }

    public static GiftStore getGiftStore() {
        return new CloudGiftStore();
    }

    public static CouponStore getCouponStore() {
        return new CloudCouponStore();
    }

    public static NoticeStore getNoticeStore() {
        return new CloudNoticeStore();
    }
}
