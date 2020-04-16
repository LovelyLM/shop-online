package com.leiming.service;

import com.leiming.mapper.CarouselMapper;
import com.leiming.pojo.Carousel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/5 23:35
 * V 1.0
 */
@Service
public class CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Carousel> queryAll(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);

        List<Carousel> result = carouselMapper.selectByExample(example);
        return result;
    }
}
