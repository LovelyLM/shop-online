package com.leiming.mapper;

import com.leiming.pojo.vo.CategoryVO;
import com.leiming.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * 2020/4/6 11:03
 * V 1.0
 * @author LeiMing
 */
public interface CategoryMapperCustom {

    /**
     * 根据根节点查询所有子节点
     * @param rootId 根节点
     * @return 统一返回值
     */
    List<CategoryVO> getSubCatList(Integer rootId);

    /**
     * 根据根节点获取6个商品
     * @param rootCatId 根节点
     * @return 统一返回值
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
