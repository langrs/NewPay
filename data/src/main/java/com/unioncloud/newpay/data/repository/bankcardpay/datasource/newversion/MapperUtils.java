package com.unioncloud.newpay.data.repository.bankcardpay.datasource.newversion;

import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;
import com.unioncloud.pax.PaxPayException;
import com.unioncloud.pax.PaxRefundRequest;
import com.unioncloud.pax.PaxResponse;
import com.unioncloud.pax.PaxSaleRequest;
import com.unioncloud.pax.PaxSaleVoidRequest;

public class MapperUtils {

    public static PaxSaleRequest mapperSaleRequest(BankcardSaleRequest request) throws PaxPayException {
        return new PaxSaleRequest(Integer.toString(request.getTransAmount()));
    }

    public static PaxSaleVoidRequest mapperSaleVoidRequest(BankcardRefundRequest request) throws PaxPayException {
        return new PaxSaleVoidRequest(request.getOriVoucherNo());
    }

    public static PaxRefundRequest mapperRefundRequest(BankcardRefundRequest request) throws PaxPayException {
        return new PaxRefundRequest(Integer.toString(request.getTransAmount()),
                request.getOriVoucherNo(),  // TODO 应该传递交易参考号. 而不是交易流水
                request.getOriData());
    }

    public static BankcardSaleResult mapperSaleResponse(PaxResponse response) {
        BankcardSaleResult result = new BankcardSaleResult();
        result.setRespCode(response.getRspCode());
        result.setRespMessage(response.getRspMsg());
        result.setAmount(Integer.valueOf(response.getTransAmount()));
        result.setCardNo(response.getCardNo());
        result.setBatchNo(response.getBatchNo());
        result.setReferNo(response.getRefNo());
        result.setVoucherNo(response.getVoucherNo());
        return result;
    }

    public static BankcardRefundResult mapperRefundResponse(PaxResponse response) {
        BankcardRefundResult result = new BankcardRefundResult();
        result.setRespCode(response.getRspCode());
        result.setRespMessage(response.getRspMsg());
        result.setAmount(Integer.valueOf(response.getTransAmount()));
        result.setBatchNo(response.getBatchNo());
        result.setReferNo(response.getRefNo());
        result.setVoucherNo(response.getVoucherNo());
        return result;
    }
}
