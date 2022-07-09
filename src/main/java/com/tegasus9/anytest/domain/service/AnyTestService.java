package com.tegasus9.anytest.domain.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tegasus9.anytest.infrastructrue.dao.TestUserMapper;
import com.tegasus9.anytest.infrastructrue.po.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Tegasus9
 * @date 2022/7/9 10:53
 * @description
 */
@Slf4j
@Service
public class AnyTestService {
    @Resource
    private TestUserMapper  testUserMapper;
    public String testMapper(String userPhone) {
        log.info("测试mapper...");
        log.info("userPhone = {}", userPhone);
        List<TestUser> testUsers = testUserMapper.selectAllByUserPhone(userPhone);
        return JSON.toJSONString(testUsers, SerializerFeature.WriteDateUseDateFormat);
    }
}
