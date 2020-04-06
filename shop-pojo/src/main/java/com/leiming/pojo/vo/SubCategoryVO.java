package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by LovelyLM
 * 2020/4/6 10:44
 * V 1.0
 * 二级分类一下所有分类，及三级分类
 */
@Getter
@Setter
class SubCategoryVO {
    private Integer subId;
    private String SubName;
    private String subType;
    private String subFatherId;
}
