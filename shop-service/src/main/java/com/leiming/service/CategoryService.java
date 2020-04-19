package com.leiming.service;

import com.leiming.mapper.CategoryMapper;
import com.leiming.mapper.CategoryMapperCustom;
import com.leiming.pojo.Category;
import com.leiming.pojo.vo.CategoryVO;
import com.leiming.pojo.vo.NewItemsVO;
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
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    /**
     * 查询所有根节点分类
     * @return Category 分类model
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> queryAllCats() {

        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }


    /**
     * 根据根节点查询所有子节点分类
     * @param rootId 根节点分类id
     * @return List<CategoryVO> 子节点vo，这里子节点有两层
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CategoryVO> querySubCatList(Integer rootId) {
        return categoryMapperCustom.getSubCatList(rootId);
    }

    /**
     * 根据节点分类获取最新6个商品
     * @param rootCatId 节点分类
     * @return List<NewItemsVO> 最新商品vo
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        return categoryMapperCustom.getSixNewItemsLazy(rootCatId);
    }
}
