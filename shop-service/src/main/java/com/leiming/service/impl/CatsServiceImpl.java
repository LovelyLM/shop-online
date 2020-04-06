package com.leiming.service.impl;

import com.leiming.mapper.CategoryMapper;
import com.leiming.mapper.CategoryMapperCustom;
import com.leiming.pojo.Category;
import com.leiming.pojo.vo.CategoryVO;
import com.leiming.pojo.vo.NewItemsVO;
import com.leiming.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/6 0:11
 * V 1.0
 */
@Service
public class CatsServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    /**
     * 查询所有分类
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllCats() {

        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> querySubCatList(Integer RootId) {
        return categoryMapperCustom.getSubCatList(RootId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        return categoryMapperCustom.getSixNewItemsLazy(rootCatId);
    }
}
