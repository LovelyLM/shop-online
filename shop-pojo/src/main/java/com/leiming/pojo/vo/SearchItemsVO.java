package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by LovelyLM
 * 2020/4/18 1:39
 * V 1.0
 * 搜索结果vo
 */
@Getter
@Setter
public class SearchItemsVO {
    private String itemId;
    private String itemName;
    private int sellCounts;
    private String imgUrl;
    private int price;
}
