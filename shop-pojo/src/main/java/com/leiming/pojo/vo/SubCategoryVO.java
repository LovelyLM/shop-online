package com.leiming.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create by LovelyLM
 * 2020/4/6 10:44
 * V 1.0
 * 二级分类一下所有分类，及三级分类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class SubCategoryVO {
    private Integer subId;
    private String subName;
    private String subType;
    private String subFatherId;
}
