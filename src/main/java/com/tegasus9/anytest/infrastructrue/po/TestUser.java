package com.tegasus9.anytest.infrastructrue.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
    * 用户测试表
    */
@Data
public class TestUser {
    private Long userId;

    /**
    * 用户名称
    */
    private String userName;

    /**
    * 用户手机
    */
    private String userPhone;

    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}