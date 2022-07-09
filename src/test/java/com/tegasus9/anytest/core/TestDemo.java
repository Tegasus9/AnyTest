package com.tegasus9.anytest.core;

import com.tegasus9.anytest.infrastructrue.dao.TestUserMapper;
import com.tegasus9.anytest.infrastructrue.po.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Tegasus9
 * @date 2022/7/9 11:03
 * @description
 */
@Slf4j
public class TestDemo extends BaseTest{
    @Resource
    TestUserMapper testUserMapper;
    @Test
    public void businessTestDemo() {
        // 任意测试
        log.info("test start!");
        TestUser testUser = new TestUser();
        testUser.setUserName("testName");
        testUser.setUserPhone("123");
        testUserMapper.insertSelective(testUser);
        List<TestUser> testUsers = testUserMapper.selectAllByUserPhone("123");
        TestUser testUser1 = testUsers.get(0);
        Assertions.assertEquals("testName",testUser1.getUserName());
        log.info("test end!");
    }

    @Override
    protected String getDataSouce() {
        return "default";
    }

    @Override
    protected String initSqlScriptFilePath() {
        return "test.sql";
    }

    @Override
    protected DataTestSqlObject initDataTestSqlObject() {
        DataTestSqlObject dataTestSqlObject = new DataTestSqlObject();

        // 设置需要处理为临时表的表
        List<String> tableNameSet = new ArrayList<>();
        tableNameSet.add("test_user");

        // 设置后缀 单元测试中 会将 tableNameSet 中的表 经由 插件 修改为 base_user_test 及 base_order_test
        dataTestSqlObject.setSuffix("_test");
        dataTestSqlObject.setTableNameSet(tableNameSet);

        return dataTestSqlObject;
    }
}
