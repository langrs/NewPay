package com.unioncloud.newpay.data.entity.login;

import com.google.gson.annotations.SerializedName;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.param.CodeEntity;
import com.unioncloud.newpay.data.entity.param.PosExtraParmEntity;
import com.unioncloud.newpay.data.entity.paymethod.PayMethodEntity;
import com.unioncloud.newpay.data.entity.paymethod.PayMethodGroupEntity;
import com.unioncloud.newpay.data.entity.pos.PosEntity;

import java.util.List;

/**
 * Created by cwj on 16/8/8.
 */
public class LoginResultEntity {

    @SerializedName("posInfo")
    PosEntity pos;
    @SerializedName("payView")
    List<PayMethodGroupEntity> payMethodGroupList;
    @SerializedName("paymode")
    List<PayMethodEntity> payMethodEntityList;
    @SerializedName("posParm")
    List<PosExtraParmEntity> extraParmList;
    @SerializedName("code")
    List<CodeEntity> codeList;

    public PosEntity getPos() {
        return pos;
    }

    public void setPos(PosEntity pos) {
        this.pos = pos;
    }

    public List<PayMethodGroupEntity> getPayMethodGroupList() {
        return payMethodGroupList;
    }

    public void setPayMethodGroupList(List<PayMethodGroupEntity> payMethodGroupList) {
        this.payMethodGroupList = payMethodGroupList;
    }

    public List<PayMethodEntity> getPayMethodEntityList() {
        return payMethodEntityList;
    }

    public void setPayMethodEntityList(List<PayMethodEntity> payMethodEntityList) {
        this.payMethodEntityList = payMethodEntityList;
    }

    public List<PosExtraParmEntity> getExtraParmList() {
        return extraParmList;
    }

    public void setExtraParmList(List<PosExtraParmEntity> extraParmList) {
        this.extraParmList = extraParmList;
    }

    public List<CodeEntity> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CodeEntity> codeList) {
        this.codeList = codeList;
    }
}
