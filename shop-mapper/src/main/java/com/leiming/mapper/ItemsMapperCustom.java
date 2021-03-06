package com.leiming.mapper;

import com.leiming.pojo.vo.ItemCommentVO;
import com.leiming.pojo.vo.SearchItemsVO;
import com.leiming.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Create by LovelyLM
 * 2020/4/17 3:42
 * V 1.0
 * @author Leiming
 */
public interface ItemsMapperCustom {
    List<ItemCommentVO> queryItemComments(@Param(value = "paramsMap") Map<String, Object> paramsMap);
    List<SearchItemsVO> searchItems(@Param(value = "paramsMap") Map<String, Object> paramsMap);
    List<SearchItemsVO> searchItemsByThirdCat(@Param(value = "paramsMap") Map<String, Object> paramsMap);
    List<ShopcartVO> queryItemsBySpecIds(@Param(value = "paramsList") List<String> paraList);
    int decreaseItemSpecStock(@Param(value = "specId") String specId, @Param(value = "pendingCounts") Integer pendingCounts);
}
