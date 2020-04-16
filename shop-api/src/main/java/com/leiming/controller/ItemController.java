package com.leiming.controller;

import com.leiming.pojo.*;
import com.leiming.pojo.vo.CommentLeveCountsVO;
import com.leiming.pojo.vo.ItemInfoVO;
import com.leiming.service.ItemService;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/6 22:54
 * V 1.0
 */

@RestController
@RequestMapping("items")
@Api(value = "商品", tags = {"商品相关"})
public class ItemController {
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "获取首页分类列表", notes = "获取首页分类列表", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JsonResult subCat(@PathVariable String itemId){
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemParams(itemsParam);
        itemInfoVO.setItemSpecList(itemsSpecs);

        return JsonResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "获取商品的评论数", notes = "获取商品的评论数", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JsonResult commentLevel(@RequestParam String itemId){
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }
        CommentLeveCountsVO countsVO = itemService.queryCommentCount(itemId);
        return JsonResult.ok(countsVO);

    }
}
