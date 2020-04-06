package com.leiming.mapper;

import com.leiming.pojo.vo.CategoryVO;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/6 11:03
 * V 1.0
 */
public interface CategoryMapperCustom {
    List<CategoryVO> getSubCatList(Integer rootId);

    List getSixNewItemsLazy(Integer rootCatId);
}
