package com.unioncloud.newpay.presentation.model.checkout;

import com.raizlabs.coreutils.events.Event;
import com.raizlabs.coreutils.functions.Delegate;
import com.unioncloud.newpay.domain.model.erp.VipCard;

/**
 * Created by cwj on 16/8/10.
 */
public class SelectedVipCard {

    private Event<SelectedVipCard> dataChanged = new Event<>();
    private VipCard vipCard;
    private Boolean isPoint = Boolean.FALSE;  // 是否允许积分

    public void addDataChangedListener(Delegate<SelectedVipCard> listener) {
        getDataChangedEvent().addListener(listener);
    }

    public void removeDataChangedListener(Delegate<SelectedVipCard> listener) {
        getDataChangedEvent().removeListener(listener);
    }

    public Event<SelectedVipCard> getDataChangedEvent() {
        return this.dataChanged;
    }

    public void clear() {
        vipCard = null;
        isPoint = Boolean.FALSE;
        notifyDataChanged(this);
    }

    public void setSelectedVipCard(VipCard vipCard) {
        setSelectedVipCard(vipCard, true);
    }

    public void setSelectedVipCard(VipCard vipCard, boolean isPoint) {
        if (vipCard != null) {
            // 避免设置后,外部持有的引用修改对象
            this.vipCard = new VipCard(vipCard);
        } else {
            this.vipCard = null;
        }
        this.isPoint = isPoint;
        notifyDataChanged(this);
    }

    public VipCard getVipCard() {
        return vipCard;
    }

    public boolean isPoint() {
        return isPoint;
    }

    public void notifyDataChanged(Object object) {
        getDataChangedEvent().raiseEvent(this);
    }
}
