package com.leiming.service;

import com.leiming.pojo.Carousel;

import java.util.List;

/**
 * Create by LovelyLM
 * 2020/4/5 23:34
 * V 1.0
 */
public interface CarouselService {
    List<Carousel> queryAll(Integer isShow);
}
