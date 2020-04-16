package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by LovelyLM
 * 2020/4/7 22:10
 * V 1.0
 * 用于展示商品评价数的vo
 */
@Getter
@Setter
public class CommentLeveCountsVO {

    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
