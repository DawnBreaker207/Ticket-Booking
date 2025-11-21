package com.dawn.backend.config.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
public class MomoConfig {

    @Value("${payment.Momo.partnerCode}")
    private String momo_PartnerCode;

    @Value("${payment.Momo.url}")
    private String momo_PayUrl;

    @Value("${payment.Momo.accessKey}")
    private String momo_AccessKey;

    @Value("${payment.Momo.secretKey}")
    private String momo_SecretKey;

    @Value("${payment.Momo.returnUrl}")
    private String momo_RedirectUrl;

    @Value("${payment.Momo.orderType}")
    private String momo_OrderType;

    @Value("${payment.Momo.notifyUrl}")
    private String momo_IpnUrl;

    public Map<String, String> getMomoConfig() {
        Map<String, String> momoParamsMap = new HashMap<>();
        momoParamsMap.put("partnerCode", this.momo_PartnerCode);
        momoParamsMap.put("requestType", this.momo_OrderType);
        momoParamsMap.put("ipnUrl", this.momo_IpnUrl);
        momoParamsMap.put("redirectUrl", this.momo_RedirectUrl);
        momoParamsMap.put("lang", "vi");

        return momoParamsMap;
    }
}
