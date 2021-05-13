package com.leiming.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

/**
 * @author LeiMing
 */
@Setter
@Getter
@ToString
public class ShopcartBO {
    /**
     * 用户id
     */
    private String userId;
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    /**
     * 商品数量
     */
    @Min(value = 1, message = "购买数量至少为1")
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;


}
