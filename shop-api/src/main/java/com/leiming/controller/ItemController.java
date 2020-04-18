package com.leiming.controller;

import cn.hutool.core.util.StrUtil;
import com.leiming.pojo.*;
import com.leiming.pojo.vo.CommentLeveCountsVO;
import com.leiming.pojo.vo.ItemInfoVO;
import com.leiming.service.ItemService;
import com.leiming.utils.JsonResult;
import com.leiming.utils.PagedGridResult;
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
public class ItemController extends BaseController{
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

    @ApiOperation(value = "获取商品评论内容", notes = "获取商品评论内容", httpMethod = "GET")
    @GetMapping("/comments")
    public JsonResult getComments(@RequestParam String itemId,
                                  @RequestParam Integer level,
                                  @RequestParam Integer page,
                                  @RequestParam Integer pageSize){
        if (StrUtil.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.queryPagedComments(itemId, level, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "搜索商品列表（根据商品名字）", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JsonResult search(@RequestParam String keywords,
                                  @RequestParam String sort,
                                  @RequestParam Integer page,
                                  @RequestParam Integer pageSize){
        if (StrUtil.isBlank(keywords)) {
            return JsonResult.errorMsg("搜索内容不能为空");
        }
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = PAGE_SIZE;//默认20页
        }
        PagedGridResult pagedGridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "搜索商品列表（根据分类id）", notes = "搜索商品列表（根据分类id）", httpMethod = "GET")
    @GetMapping("/catItems")
    public JsonResult search(@RequestParam Integer catId,
                             @RequestParam String sort,
                             @RequestParam Integer page,
                             @RequestParam Integer pageSize){
        if (catId == null) {
            return JsonResult.errorMsg("分类id不能为空");
        }
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = PAGE_SIZE;//默认20页
        }
        PagedGridResult pagedGridResult = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }
}
