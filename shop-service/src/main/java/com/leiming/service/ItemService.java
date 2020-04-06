package com.leiming.service;

import com.leiming.pojo.Items;
import com.leiming.pojo.ItemsImg;
import com.leiming.pojo.ItemsParam;
import com.leiming.pojo.ItemsSpec;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/6 22:41
 * V 1.0
 */
public interface ItemService {

    /**
     * 根据商品id查询商品
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    List<ItemsImg> queryItemList(String itemId);

    List<ItemsSpec> queryItemSpecList(String itemId);

    ItemsParam queryItemParam(String itemId);

}
