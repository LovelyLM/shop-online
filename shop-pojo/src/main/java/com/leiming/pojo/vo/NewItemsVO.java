package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 最新商品VO
 */
@Getter
@Setter
public class NewItemsVO {

    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;

    private List<SimpleItemVO> simpleItemList;

}
