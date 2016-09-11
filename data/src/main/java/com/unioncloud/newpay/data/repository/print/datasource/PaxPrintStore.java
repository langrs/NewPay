package com.unioncloud.newpay.data.repository.print.datasource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.unioncloud.newpay.data.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by cwj on 16/8/19.
 */
public class PaxPrintStore implements PrintStore {

    private Context context;

    public PaxPrintStore(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Boolean> printOrder(final PrintOrderInfo orderInfo) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean isSuccess = print(orderInfo);
                subscriber.onNext(isSuccess);
            }
        }) ;
    }

    private boolean print(PrintOrderInfo info) {
//        View view = LayoutInflater.from(context).inflate(R.layout.print_order, null);
//        TextView posno = (TextView) view.findViewById(R.id.print_order_posno);
//        posno.setText(info.getPosInfo().getPosNumber());
//        TextView userno = (TextView) view.findViewById(R.id.print_order_userno);
//        userno.setText(info.getPosInfo().getUserNumber());
//        TextView time = (TextView) view.findViewById(R.id.print_order_time);
//        time.setText(info.getSaleOrderResult().getSaleDate());
//        TextView orderId = (TextView) view.findViewById(R.id.print_order_orderId);
//        orderId.setText(info.getOrderId());
//        TextView reprint = (TextView) view.findViewById(R.id.print_order_reprint);
//        if (info.isReprint()) {
//            reprint.setVisibility(View.VISIBLE);
//        } else {
//            reprint.setVisibility(View.GONE);
//        }
//        ListView listView = (ListView) view.findViewById(R.id.print_order_items);
//        listView.setAdapter(new CartItemAdapter(context, info.getCartItemList()));
//        int flag = PrinterUtils.printView(view);
//        if (flag == 0) {
//            return true;
//        }
        return false;
    }

//    private static class CartItemAdapter extends BaseAdapter {
//
//        List<CartItem> list;
//        Context context;
//
//        public CartItemAdapter(Context context, List<CartItem> list) {
//            this.list = list;
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            CartItemHolder holder;
//            if (convertView == null) {
//                holder = new CartItemHolder();
//                convertView = LayoutInflater.from(context).inflate(R.layout.print_order_product_item, parent, false);
//                holder.name = (TextView) convertView.findViewById(R.id.print_order_product_item_name);
//                holder.quantity = (TextView) convertView.findViewById(R.id.print_order_product_item_quantity);
//                holder.unitPrice = (TextView) convertView.findViewById(R.id.print_order_product_item_unitprice);
//                holder.total = (TextView) convertView.findViewById(R.id.print_order_product_item_subtotal);
//                convertView.setTag(holder);
//            } else {
//                holder = (CartItemHolder) convertView.getTag();
//            }
//            CartItem item = list.get(position);
//            holder.name.setText(item.getProductName() + "\n" + item.getProductNumber());
//            holder.quantity.setText(String.format("%.2f", item.getQuantity()));
//            holder.unitPrice.setText(MoneyUtils.fenToString(item.getSellUnitPrice()));
//            holder.total.setText(MoneyUtils.fenToString(item.getSellUnitPrice() * item.getQuantity()));
//            return convertView;
//        }
//    }
//
//    static class CartItemHolder {
//        public TextView name;
//        public TextView quantity;
//        public TextView unitPrice;
//        public TextView total;
//    }
}
