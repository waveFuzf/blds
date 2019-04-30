package com.example.blds.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
@Component
public class AlipayUtil {

    @Value("${alipay.inner}")
    private String innerKey;
    @Value("${alipay.public}")
    private String publicKey;

    private AlipayClient alipayClient;



    public String getPayUrl(String fee, String orderId, String consult_id) throws AlipayApiException {
        if (alipayClient==null){
            alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                    "2016092200570253",innerKey,"json","UTF-8",publicKey,"RSA2");
        }
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        request.setReturnUrl("http://129.204.205.30:8080/dist/index.html#/blConsultInfo?consult_id="+consult_id+"&doctorType=0");//todo
        request.setNotifyUrl("http://129.204.205.30:8080/paynotify");
        model.setBody("病理大师");
        model.setSubject("问诊费支付");
        model.setOutTradeNo(orderId);
        model.setTimeoutExpress("150m");
        model.setTotalAmount(fee);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(model);
        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean refundPay(String fee,String outTradeNo) throws AlipayApiException {
        if (alipayClient==null){
            alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                    "2016092200570253",innerKey,"json","UTF-8",publicKey,"RSA2");
        }
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setRefundAmount(fee);
        model.setOutTradeNo(outTradeNo);
        request.setBizModel(model);
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        return response.isSuccess();
    }

    public Boolean getPayStatus(String orderNo, String trade_no) throws AlipayApiException {
        if (alipayClient==null){
            alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                    "2016092200570253",innerKey,"json","UTF-8",publicKey,"RSA2");
        }
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject obj=new JSONObject();
        obj.put("out_trade_no",orderNo);
        obj.put("trade_no",trade_no);
        request.setBizContent(obj.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        System.out.println(response);
        return JSONObject.fromObject(response.getBody()).optJSONObject("alipay_trade_query_response").optString("msg").equals("Success");
    }

    public String getAutoInfo(String autoCode) {
        if (alipayClient==null){
            alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                    "2016092200570253",innerKey,"json","UTF-8",publicKey,"RSA2");
        }
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(autoCode);
        request.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse oauthTokenResponse=null;
        try {
            oauthTokenResponse= alipayClient.execute(request);
            oauthTokenResponse.getUserId();
            AlipayUserInfoShareRequest userInfoRequest = new AlipayUserInfoShareRequest();
            String auth_token = oauthTokenResponse.getAccessToken();
            try {
                AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(userInfoRequest, auth_token);
                return userinfoShareResponse.getBody();
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String payMoneyToDoctor(String outBizNo,String account,String amount,String blNo) throws AlipayApiException {
        if (alipayClient==null){
            alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                    "2016092200570253",innerKey,"json","UTF-8",publicKey,"RSA2");
        }
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model =new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(outBizNo);
        model.setPayeeType("ALIPAY_USERID");
        model.setPayeeAccount(account);
        model.setAmount(amount);
        model.setPayerShowName("远程病理"+blNo+"就诊费");
        request.setBizModel(model);
        AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            return response.getOrderId();
        } else {
            System.out.println("调用失败");
            return null;
        }
    }
}
