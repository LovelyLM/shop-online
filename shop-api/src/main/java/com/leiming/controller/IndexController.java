package com.leiming.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/5 23:43
 * V 1.0
 * @author LeiMing
 */
@RestController
@RequestMapping("index")
@Api(value = "首页", tags = {"首页展示的相关"})
public class IndexController {
    @Resource
    private CarouselService carouselService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult carousel(){

        String carousels = (String)redisTemplate.opsForValue().get("carousel");

        List<Carousel> carouselList;
        if (StrUtil.isEmpty(carousels)){
            carouselList = carouselService.queryAll(YesOrNo.YSE.type);
            String s = JSONUtil.toJsonStr(carouselList);
            redisTemplate.opsForValue().set("carousel", JSONUtil.toJsonStr(carouselList));

        } else {
            //先将String转换为jsonArray，再将jsonArray转换为java List对象
            JSONArray jsonArray = JSONUtil.parseArray(carousels);
            carouselList = JSONUtil.toList(jsonArray, Carousel.class);
        }

        return JsonResult.ok(carouselList);

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
