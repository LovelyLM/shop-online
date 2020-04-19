package com.leiming.enums;

import lombok.AllArgsConstructor;

/**
 * Create by LovelyLM
 * 2020/3/31 23:12
 * V 1.0
 * @author Leiming
 */

@AllArgsConstructor
public enum CommentLevel {
    /**
     *
     */

    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    public final Integer type;
    public final String value;


}
