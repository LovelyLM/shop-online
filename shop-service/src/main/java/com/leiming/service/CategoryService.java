package com.leiming.service;

import com.leiming.pojo.Category;
import com.leiming.pojo.vo.CategoryVO;
import com.leiming.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/6 0:10
 * V 1.0
 */
public interface CategoryService {

    /**
     * 查询所有根分类
     * @return
     */
    List<Category> queryAllCats();

    /**
     * 根据目录分类查询子分类
     * @param RootId
     * @return
     */
    List<CategoryVO> querySubCatList(Integer RootId);

    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
