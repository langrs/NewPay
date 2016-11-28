package com.unioncloud.newpay.data.entity.cart;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by cwj on 11/27/16.
 */
@DatabaseTable(tableName = "hs_shopcart")
public class CartEntity {
    private String saleNo;          // 销售单号
    private String saleDate;        // 销售时间
    private String companyId;       // 公司ID
    private String shopId;          // 分店ID
    private String storeId;         // 商铺ID
    private String posId;           // 终端ID
    private String userId;          // 收银员ID

    private String dealType;        // 交易类型
    private String saleType;        // 销售类型 "01"正常销售单

    private String vipNo;
    private String originalPoints;  // 原积分
    private String salePoints;      // 本次销售积分

    private String originalAmt;     // 原价金额
    private String saleAmt;         // 应收总额
    private String payAmt;          // 实收总额
    private String dicAmt;          // 折扣总额(包含会员折扣)
    private String vipDicAmt;       // 会员折扣总额
    private String changeAmt;
    private String excessAmt;

    private String isTrain;         // 是否培训模式
    private String reason;          // 退货理由
    private String retAmt;          // 已退货总额
    private String ebillType;       // (是否领取)电子小票,"01"表示未领取
    private String upFlag;          // 上传标记
    private String upData;          // 上传日期;
}
