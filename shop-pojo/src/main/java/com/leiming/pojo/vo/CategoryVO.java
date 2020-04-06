package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/6 10:41
 * V 1.0
 * 二级分类vo
 */
@Getter
@Setter
public class CategoryVO {
    private Integer id;
    private String name;
    private String type;
    private String fatherId;

    private List<SubCategoryVO> subCatList;
}
