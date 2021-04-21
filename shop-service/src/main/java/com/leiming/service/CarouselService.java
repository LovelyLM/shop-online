package com.leiming.service;

import com.leiming.mapper.CarouselMapper;
import com.leiming.pojo.Carousel;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 2020/4/5 23:35
 * V 1.0
 * @author LeiMing
 */
@Service
public class CarouselService {
    @Resource
    private CarouselMapper carouselMapper;

    /**
     * 查询首页轮播图
     * @param isShow 是否显示
     * @return 返回值
     */
    public List<Carousel> queryAll(Integer isShow) {
        //构造查询条件
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);

        return carouselMapper.selectByExample(example);
    }
}
