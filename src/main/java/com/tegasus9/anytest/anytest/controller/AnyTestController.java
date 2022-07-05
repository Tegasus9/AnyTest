package com.tegasus9.anytest.anytest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiongYiGe
 * @date 2022/7/5
 * @description
 */
@RestController
@RequestMapping("/anyTest")
@Slf4j
public class AnyTestController {
    private static final Map<String,Object> STRING_OBJECT_HASH_MAP = new HashMap<>();

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
}