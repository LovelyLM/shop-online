package com.leiming.service;

import com.leiming.mapper.CategoryMapper;
import com.leiming.mapper.CategoryMapperCustom;
import com.leiming.pojo.Category;
import com.leiming.pojo.vo.CategoryVO;
import com.leiming.pojo.vo.NewItemsVO;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 2020/4/6 0:11
 * V 1.0
 * @author LeiMing
 */
@Service
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private CategoryMapperCustom categoryMapperCustom;

    /**
     * 查询所有根节点分类
     * @return Category 分类model
     */
    public List<Category> queryAllCats() {

        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        return categoryMapper.selectByExample(example);
    }


    /**
     * 根据根节点查询所有子节点分类
     * @param rootId 根节点分类id
     * @return List<CategoryVO> 子节点vo，这里子节点有两层
     */
    public List<CategoryVO> querySubCatList(Integer rootId) {
        return categoryMapperCustom.getSubCatList(rootId);
    }

    /**
     * 根据节点分类获取最新6个商品
     * @param rootCatId 节点分类
     * @return List<NewItemsVO> 最新商品vo
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        return categoryMapperCustom.getSixNewItemsLazy(rootCatId);
    }
}
