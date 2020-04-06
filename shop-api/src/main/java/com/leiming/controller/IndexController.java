package com.leiming.controller;

import com.leiming.enums.YesOrNo;
import com.leiming.pojo.Carousel;
import com.leiming.pojo.Category;
import com.leiming.pojo.vo.CategoryVO;
import com.leiming.pojo.vo.NewItemsVO;
import com.leiming.service.CarouselService;
import com.leiming.service.CategoryService;
import com.leiming.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/5 23:43
 * V 1.0
 */
@RestController
@RequestMapping("index")
@Api(value = "首页", tags = {"首页展示的相关"})
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult carousel(){
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YSE.type);
        return JsonResult.ok(carousels);

    }

    @ApiOperation(value = "获取首页分类列表", notes = "获取首页分类列表", httpMethod = "GET")
    @GetMapping("/cats")
    public JsonResult cats(){
        List<Category> cats = categoryService.queryAllCats();
        return JsonResult.ok(cats);

    }

    @ApiOperation(value = "获取子分类列表", notes = "获取子分类列表", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JsonResult subCat(@PathVariable Integer rootCatId){
        List<CategoryVO> categoryVos = categoryService.querySubCatList(rootCatId);
        return JsonResult.ok(categoryVos);

    }

    @ApiOperation(value = "查询最新6个商品", notes = "查询最新6个商品", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JsonResult sixNewItems(@PathVariable Integer rootCatId){
        if (rootCatId == null){
            return JsonResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> sixNewItems = categoryService.getSixNewItemsLazy(rootCatId);
        return JsonResult.ok(sixNewItems);

    }
}
