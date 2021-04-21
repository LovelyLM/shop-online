package com.leiming.pojo.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 2020/4/7 22:10
 * V 1.0
 * 用于展示商品评价数的vo
 * @author LeiMing
 */
@Data
@ToString
public class CommentLeveCountsVO {

    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
