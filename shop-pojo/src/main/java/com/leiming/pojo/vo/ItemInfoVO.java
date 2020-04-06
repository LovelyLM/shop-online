package com.leiming.pojo.vo;

import com.leiming.pojo.Items;
import com.leiming.pojo.ItemsImg;
import com.leiming.pojo.ItemsParam;
import com.leiming.pojo.ItemsSpec;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/6 22:59
 * V 1.0
 * 商品详情VO
 */
@Setter
@Getter
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

}
