package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 购物车vo
 * @author Leiming
 */
@Setter
@Getter
@ToString
public class ShopcartVO {

    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;
}
