package com.tegasus9.anytest.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Tegasus9
 * @date 2022/7/9 10:54
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AnyTestService.class)
public class AnyTestServiceTest {
    @Resource
    private AnyTestService anyTestService;

    @Test
    public void testMapper() {
        String s = anyTestService.testMapper("18888888888");
    }
}