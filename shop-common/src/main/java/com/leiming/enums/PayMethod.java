package com.leiming.enums;

import lombok.AllArgsConstructor;

/**
 * 支付方式枚举
 * @author Leiming
 */
@AllArgsConstructor
public enum PayMethod {

    /**
     * 支付方式
     */
    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;

}
