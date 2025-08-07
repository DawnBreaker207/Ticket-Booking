package com.example.backend.config.payment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.example.backend.util.VNPayUtils;

@Configuration
public class VNPayConfig {

    @Value("${payment.VNPay.url}")
    private String vnp_PayUrl;

    @Value("${payment.VNPay.returnUrl}")
    private String vnp_ReturnUrl;

    @Value("${payment.VNPay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${payment.VNPay.secretKey}")
    private String secretKey;

    @Value("${payment.VNPay.version}")
    private String vnp_Version;

    @Value("${payment.VNPay.command}")
    private String vnp_Command;

    @Value("${payment.VNPay.orderType}")
    private String orderType;

    public String getVnp_PayUrl() {
	return vnp_PayUrl;
    }

    public String getVnp_secretKey() {
	return secretKey;
    }

    public Map<String, String> getVNPayConfig() {
	Map<String, String> vnpParamsMap = new HashMap<>();
	vnpParamsMap.put("vnp_Version", this.vnp_Version);
	vnpParamsMap.put("vnp_Command", this.vnp_Command);
	vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
	vnpParamsMap.put("vnp_CurrCode", "VND");
	vnpParamsMap.put("vnp_TxnRef", VNPayUtils.getRandomNumber(8));
	vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtils.getRandomNumber(8));
	vnpParamsMap.put("vnp_OrderType", this.orderType);
	vnpParamsMap.put("vnp_Locale", "vn");
	vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	String vnpCreateDate = formatter.format(calendar.getTime());
	vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
	calendar.add(Calendar.MINUTE, 15);
	String vnp_ExpireDate = formatter.format(calendar.getTime());
	vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);

	return vnpParamsMap;
    }

}
