package com.leiming.mapper;

import com.leiming.pojo.vo.ItemCommentVO;

import java.util.List;
import java.util.Map;

/**
 * Create by LovelyLM
 * 2020/4/17 3:42
 * V 1.0
 */
public interface ItemsMapperCustom {
    List<ItemCommentVO> queryItemComments(Map<String, Object> paramsMap);
}
