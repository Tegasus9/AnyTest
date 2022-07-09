package com.tegasus9.anytest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tegasus9.anytest.infrastructrue.dao.TestUserMapper;
import com.tegasus9.anytest.infrastructrue.po.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tegasus9
 * @date 2022/7/5
 * @description
 */
@RestController
@RequestMapping("/anyTest")
@Slf4j
public class AnyTestController {
    private static final Map<String,Object> STRING_OBJECT_HASH_MAP = new HashMap<>();

    @Resource
    private TestUserMapper testUserMapper;
    /**
     * OOMTest
     * 运行时VM参数：-Xms32M -Xmx32M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:/tmp
     * @return
     */
    @RequestMapping("/testOOM")
    public String test() {
        for (int i = 0; i < 10000; i++) {
            STRING_OBJECT_HASH_MAP.put(String.valueOf(i), new char[1024*1024]);
            log.info("尝试OOM中...");
        }
        return "OOMfail";
    }

    @RequestMapping(value = "/testMapper",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String testMapper(String userPhone) {
        log.info("测试mapper...");
        log.info("userPhone = {}", userPhone);
        List<TestUser> testUsers = testUserMapper.selectAllByUserPhone(userPhone);
        return JSON.toJSONString(testUsers, SerializerFeature.WriteDateUseDateFormat);
    }
}