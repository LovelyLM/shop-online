package com.leiming.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Create by LovelyLM
 * 2020/4/17 3:38
 * V 1.0
 * 展示商品评论信息vo
 */
@Setter
@Getter
public class ItemCommentVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;

}
