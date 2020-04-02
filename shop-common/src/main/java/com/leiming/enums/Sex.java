package com.leiming.enums;

import lombok.AllArgsConstructor;

/**
 * Create by LovelyLM
 * 2020/3/31 23:12
 * V 1.0
 */
@AllArgsConstructor
public enum Sex {

    WOMAN(0, "女"),
    MAN(1, "男"),
    SECRET(2, "保密");

    public final Integer type;
    public final String value;


}
