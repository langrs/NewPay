package com.unioncloud.newpay.presentation.presenter;

import android.content.Context;

import com.unioncloud.newpay.data.repository.appupgrade.AppDateRepository;
import com.unioncloud.newpay.data.repository.bankcardpay.BankcardPayDataRepository;
import com.unioncloud.newpay.data.repository.cart.CartDataRepository;
import com.unioncloud.newpay.data.repository.checkout.CheckoutDataRepository;
import com.unioncloud.newpay.data.repository.coupon.CouponDataRepository;
import com.unioncloud.newpay.data.repository.gift.GiftDataRepositroy;
import com.unioncloud.newpay.data.repository.login.LoginDataRepository;
import com.unioncloud.newpay.data.repository.notice.NoticeDataRepository;
import com.unioncloud.newpay.data.repository.order.OrderDataRepository;
import com.unioncloud.newpay.data.repository.pos.PosDataRepository;
import com.unioncloud.newpay.data.repository.print.PrintDataRepository;
import com.unioncloud.newpay.data.repository.product.ProductDataRepository;
import com.unioncloud.newpay.data.repository.right.RightDateRepository;
import com.unioncloud.newpay.data.repository.thirdparty.ThirdPartyDataRepository;
import com.unioncloud.newpay.data.repository.vip.VipDataRepository;
import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.repository.AppUpgradeRepository;
import com.unioncloud.newpay.domain.repository.BankcardTradeRepository;
import com.unioncloud.newpay.domain.repository.CartRepository;
import com.unioncloud.newpay.domain.repository.CheckoutRepository;
import com.unioncloud.newpay.domain.repository.CouponRepository;
import com.unioncloud.newpay.domain.repository.GiftRepository;
import com.unioncloud.newpay.domain.repository.LoginRepository;
import com.unioncloud.newpay.domain.repository.NoticeRepository;
import com.unioncloud.newpay.domain.repository.PosRepository;
import com.unioncloud.newpay.domain.repository.PrintRepository;
import com.unioncloud.newpay.domain.repository.ProductRepository;
import com.unioncloud.newpay.domain.repository.RightRepository;
import com.unioncloud.newpay.domain.repository.SaleOrderRepository;
import com.unioncloud.newpay.domain.repository.ThirdPartyRepository;
import com.unioncloud.newpay.domain.repository.VipRepository;
import com.unioncloud.newpay.executor.DefaultExecutorProvider;

/**
 * Created by cwj on 16/8/10.
 */
public class PresenterUtils {

    public static ExecutorProvider getExecutorProvider() {
        return DefaultExecutorProvider.getInstance();
    }

    public static PosRepository getPosRepository() {
        return new PosDataRepository();
    }

    public static LoginRepository getLoginRepository() {
        return new LoginDataRepository();
    }

    public static ProductRepository getProductRepository() {
        return new ProductDataRepository();
    }

    public static VipRepository getVipRepository() {
        return new VipDataRepository();
    }

    public static CartRepository getCartDataRepository() {
        return new CartDataRepository();
    }

    public static CheckoutRepository getCheckoutRepository() {
        return new CheckoutDataRepository();
    }

    public static SaleOrderRepository getSaleOrderRepository() {
        return new OrderDataRepository();
    }

    public static BankcardTradeRepository getBankcardPayRepository(Context context) {
        return new BankcardPayDataRepository(context);
    }

    public static ThirdPartyRepository getThirdPartyRepository() {
        return new ThirdPartyDataRepository();
    }

    public static AppUpgradeRepository getAppUpgradeRepository() {
        return new AppDateRepository();
    }

    public static GiftRepository getGiftRepository() {
        return new GiftDataRepositroy();
    }

    public static CouponRepository getCouponRepository() {
        return new CouponDataRepository();
    }

    public static NoticeRepository getNoticeRepository(Context context) {
        return new NoticeDataRepository(context);
    }

    public static RightRepository getRightRepository() {
        return new RightDateRepository();
    }

    public static PrintRepository getPrintRepository(Context context) {
        return new PrintDataRepository(context);
    }
}
