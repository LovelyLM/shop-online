package com.leiming.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leiming.enums.CommentLevel;
import com.leiming.enums.YesOrNo;
import com.leiming.mapper.*;
import com.leiming.pojo.*;
import com.leiming.pojo.vo.CommentLeveCountsVO;
import com.leiming.pojo.vo.ItemCommentVO;
import com.leiming.pojo.vo.SearchItemsVO;
import com.leiming.pojo.vo.ShopcartVO;
import com.leiming.utils.DesensitizationUtil;
import com.leiming.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Create by LovelyLM
 * 2020/4/6 22:44
 * V 1.0
 * @author Leiming
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
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String ,Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);
        /**
         * 开始分页
         */
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> itemCommentVOS = itemsMapperCustom.queryItemComments(map);
        for (ItemCommentVO itemCommentVO:itemCommentVOS) {
            itemCommentVO.setNickname(DesensitizationUtil.commonDisplay(itemCommentVO.getNickname()));
        }
        return setPageGrid(itemCommentVOS, page);
    }

    /**
     * 设置分页数据封装
     * @param list 要分页的对象
     * @param page 页数
     * @return PagedGridResult 前端需要的分页对象
     */
    private PagedGridResult setPageGrid(List<?> list, Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setPage(page);
        gridResult.setRows(list);
        gridResult.setTotal(pageList.getPages());
        gridResult.setRecords(pageList.getTotal());
        return gridResult;
    }


    /**
     * 根据商品id以及评论等级获取评论数量
     * @param itemId 商品id
     * @param level 评论等级，中、好、差评
     * @return Integer
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Integer getCommentCounts(String itemId, Integer level) {
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

    /**
     * 根据商品名字查询商品（模糊查询）
     * @param keywords 搜索关键字
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize){
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOList = itemsMapperCustom.searchItems(map);

        return setPageGrid(searchItemsVOList, page);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize){
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOList = itemsMapperCustom.searchItemsByThirdCat(map);

        return setPageGrid(searchItemsVOList, page);
    }

    /**
     *
     * @param specIds
     * @return List
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ShopcartVO> queryItemsBySpecIds(String specIds){
        List<String> split = StrUtil.split(specIds, ',');
        return itemsMapperCustom.queryItemsBySpecIds(split);
    }

    /**
     * 根据规格id查询规格
     * @param itemSpecId 规格id
     * @return ItemsSpec 规格model
     */
    public ItemsSpec queryItemSpecById(String itemSpecId) {

        return itemsSpecMapper.selectByPrimaryKey(itemSpecId);
    }

    /**
     * 根据商品id查询商品主图
     * @param itemId 商品id
     * @return String
     */
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YSE.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    /**
     * 减库存
     * @param specId
     * @param buyCounts
     */
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }

}
