package com.example.blds.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayUploadRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.controller.bl.BlInquiry;
import com.example.blds.service.HzConsultService;
import com.example.blds.util.AlipayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
public class PayController {
    private final Logger logger = LoggerFactory.getLogger(PayController.class);
    @Autowired
    private HzConsultService consultService;
    @Autowired
    private AlipayUtil alipayUtil;
    @Value("${alipay.public}")
    private String publicKey;

    @PostMapping("paynotify")
    public void notify(HttpServletRequest request){
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            boolean flag = AlipaySignature.rsaCheckV1(params, publicKey, "UTF-8", "RSA2");
            if(flag){
                String orderNo=params.get("out_trade_no");
                if (alipayUtil.getPayStatus(orderNo,params.get("trade_no"))){
                    consultService.updatePayStatus(orderNo,params.get("trade_no"),orderNo.contains("BCJC"));
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
