package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 2020/4/18 1:39
 * V 1.0
 * 搜索结果vo
 * @author LeiMing
 */
@Getter
@Setter
public class SearchItemsVO {
    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer price;
}
