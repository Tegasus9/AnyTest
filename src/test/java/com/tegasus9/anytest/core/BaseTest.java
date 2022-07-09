package com.tegasus9.anytest.core;


import cn.hutool.core.io.FileUtil;
import com.tegasus9.anytest.AnyTestApplication;
import com.tegasus9.anytest.infrastructrue.dao.SqlMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Tegasus9
 * @date 2022/7/9 11:01
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AnyTestApplication.class,RerouteToTableInterceptor.class})
public abstract class BaseTest {
    @Resource
    protected SqlMapper sqlMapper;

    @Before
    public void initDataToDataBase(){

        DataSourceContextHolder.setDB(getDataSouce());
        DataTestSqlObject dataTestSqlObject = initDataTestSqlObject();
        if (dataTestSqlObject != null) {
            System.out.println("init data start");
            DataTestContextHolder.setDataTestSqlObject(dataTestSqlObject);

            URL resource = getClass().getClassLoader().getResource(initSqlScriptFilePath());
            assert resource != null;
            String sql = FileUtil.readString(resource, StandardCharsets.UTF_8);

            sqlMapper.directDoSql(sql);

            System.out.println("init data end");
        }
    }

    @After
    public void clearDataToDataBase() {
        DataSourceContextHolder.setDB(getDataSouce());
        DataTestSqlObject dataTestSqlObject = DataTestContextHolder.getDataTestSqlObject();

        if (dataTestSqlObject != null) {
            System.out.println("clear data start");
            String dropTempTableTemplate = "DROP TABLE IF EXISTS  ";
            for (String tableName : dataTestSqlObject.getTableNameSet()) {
                String tempDropSql = dropTempTableTemplate + tableName;
                sqlMapper.directDoSql(tempDropSql);
            }
            System.out.println("clear data end");
        }

        DataTestContextHolder.clear();
        DataSourceContextHolder.clear();
    }

    protected abstract String getDataSouce();

    protected abstract String initSqlScriptFilePath();

    protected abstract DataTestSqlObject initDataTestSqlObject();

}
