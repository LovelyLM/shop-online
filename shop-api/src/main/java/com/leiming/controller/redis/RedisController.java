package com.leiming.controller.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author LovelyLM
 * @Date 2021/4/21 021 23:21
 */
@RestController
@RequestMapping("redis")
public class RedisController {

    @Resource
    private RedisTemplate redisTemplate;




    @GetMapping("set")
    public Object set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
        return "ok";
    }

    @GetMapping("get")
    public Object get(String key){
        Object o = redisTemplate.opsForValue().get(key);
        return o;
    }

    @GetMapping("delete")
    public Object delete(String key){
        redisTemplate.delete(key);
        return "ok";
    }




}
