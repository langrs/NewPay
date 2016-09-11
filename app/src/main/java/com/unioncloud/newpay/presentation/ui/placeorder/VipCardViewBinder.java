package com.unioncloud.newpay.presentation.ui.placeorder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.VipCard;

/**
 * Created by cwj on 16/8/14.
 */
public class VipCardViewBinder {

//    public Button addVipButton;
//    public View vipInfoView;
    public TextView vipNumberTv;
//    public TextView vipNameTv;
//    public TextView vipTypeTv;
    public View container;

    public VipCardViewBinder(View view) {
//        addVipButton = (Button) view.findViewById(R.id.place_order_add_vip_button);
//        vipInfoView = view.findViewById(R.id.place_order_vipinfo_frame);
        vipNumberTv = (TextView) view.findViewById(R.id.place_order_vip_number);
//        vipNameTv = (TextView) view.findViewById(R.id.place_order_vip_name);
//        vipTypeTv = (TextView) view.findViewById(R.id.place_order_vip_type);
        container = view.findViewById(R.id.layout_place_order_vip_container);
    }

    public void selectedVipCard(VipCard card) {
        if (card == null) {
//            addVipButton.setVisibility(View.VISIBLE);
//            vipInfoView.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
        } else {
//            addVipButton.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
//            vipInfoView.setVisibility(View.VISIBLE);
            vipNumberTv.setText(card.getCardNumber());
//            vipNameTv.setText(card.getName());
//            vipTypeTv.setText(card.getCardType());
        }
    }
}
