package com.leiming.enums;

/**
 * Create by LovelyLM
 * 2020/4/5 23:45
 * V 1.0
 */
public enum YesOrNo {
    NO(0, "否"),
    YSE(1, "是");
    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value){
        this.type = type;
        this.value = value;
    }


}
