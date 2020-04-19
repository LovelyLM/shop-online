package com.leiming.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ShopcartBO {

    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    /**
     * 商品数量
     */
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;
}
