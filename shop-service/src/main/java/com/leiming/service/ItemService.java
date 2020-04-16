package com.leiming.service;

import com.leiming.enums.CommentLevel;
import com.leiming.mapper.*;
import com.leiming.pojo.*;
import com.leiming.pojo.vo.CommentLeveCountsVO;
import com.leiming.pojo.vo.ItemCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by LovelyLM
 * 2020/4/6 22:44
 * V 1.0
 */
@Service
public class ItemService {
    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    /**
     * 根据商品id查询商品
     * @param itemId 商品id
     * @return Items 商品model
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 根据商品id查询商品图片
     * @param itemId 商品id
     * @return List<ItemsImg> 商品图片model列表
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemList(String itemId) {
        Example itemsImgExp = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsImgExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(itemsImgExp);
    }

    /**
     *根据商品id查询商品规格
     * @param itemId 商品id
     * @return List<ItemsSpec> 商品规格model
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example itemsSpecExp = new Example(ItemsSpec.class);
        Example.Criteria criteria = itemsSpecExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(itemsSpecExp);
    }

    /**
     * 根据商品id查询商品属性
     * @param itemId 商品id
     * @return ItemsParam 商品属性model
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemParam(String itemId) {
        Example itemsParamExp = new Example(ItemsParam.class);
        Example.Criteria criteria = itemsParamExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        return itemsParamMapper.selectOneByExample(itemsParamExp);
    }




    /**
     * 根据商品id以及评论等级获取评论内容
     * @param itemId 商品id
     * @param level 评论等级，中、好、差评
     * @return List<ItemCommentVO> 展示商品评论信息vo
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemCommentVO> queryPagedComments(String itemId, Integer level) {
        Map<String ,Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);
        List<ItemCommentVO> itemCommentVOS = itemsMapperCustom.queryItemComments(map);
        return itemCommentVOS;
    }


    /**
     * 根据商品id以及评论等级获取评论数量
     * @param itemId 商品id
     * @param level 评论等级，中、好、差评
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments comments = new ItemsComments();
        comments.setItemId(itemId);
        if (level != null) {
            comments.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(comments);
    }

    /**
     * 根据商品id获取获取商品所有类型（好评、中评、差评）的评论数量
     * @param itemId 商品id
     * @return 评论数vo
     */
    public CommentLeveCountsVO queryCommentCount(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;
        CommentLeveCountsVO countsVO = new CommentLeveCountsVO();
        countsVO.setBadCounts(badCounts);
        countsVO.setNormalCounts(badCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setTotalCounts(totalCounts);
        return countsVO;

    }
}
