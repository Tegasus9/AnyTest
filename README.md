# AnyTest

A project for any java test.

目前打算用来做任何与JAVA后端技术学习有关的测试。也可以当做自己平时用来测试的工具SpringBoot。

更新计划：每新增一个组件新建一个分支，以便于将来随时切换和回滚。



## 2022年7月9日：

1. 引入MysqlConnector并引入Mybatis依赖
2. 引入VoidDataBaseTest。

### VoidDataBaseTest

#### 思路来源

[优雅的进行数据库相关的单元测试 - 小破屋 | SJH Blog (songjunhao.github.io)](https://songjunhao.github.io/2020/05/04/优雅的进行数据库相关的单元测试/)

至于为什么起这个名字……是因为我想给他起名字。原因的话你们往下看大概就知道。

撸这个测试原因大概是因为自己想做`Dao层测试`，但又没什么好的方法不启动整个容器。（大概原因是链接数据库需要做相应配置，而单元测试不具备这些。等我后面深入了解一下SqlSession之后再想想怎么把现有测试改造成**只启动容器框架+链接数据库**的版本。~~目前是需要启动整个容器做测试，幸好里面的Bean不多，所以启动速度秒级~~）

于是自己花了点时间按照大佬的思路复现了一下这个测试思路。(顺带一提,里面DataSourceContextHolder没有用到,也可以忽略)

用到的技术栈:*`SpringBootTest`*,*`ThreadLocal`*,*`MybatisIntecptor`*,*`Junit4`*

总体测试思路:**新建测试表,所有数据库相关操作在测试表执行,执行完毕后删除测试表.**

#### 具体思路：

1. 对所有需要`VoidTest`的Table进行复制，重命名为`table_name_${suffix}`
2. 构建Mybatis拦截器，将所有需要`VoidTest`的sql执行语句替换成对新建测试表的执行
2. 执行测试逻辑
2. 删除临时表

#### 关键类:

`BaseTest`

```java
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

    //新建临时表
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

    //删除临时表
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

```

拦截器RerouteToTableInterceptor:

```java
package com.tegasus9.anytest.core;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;
/**
 * @author Tegasus9
 * @date 2022/7/9 11:02
 * @description
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),})
public class RerouteToTableInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        Object[] invocationArgs = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) invocationArgs[0];

        Object parameter =  invocationArgs[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);

        String originalSql = boundSql.getSql();
        //System.out.println(" ->>>originalSql: " + originalSql);
        DataTestSqlObject dataTestSqlObject = DataTestContextHolder.getDataTestSqlObject();
        List<String> tableNameSet = dataTestSqlObject.getTableNameSet();
        String newSql = null;
        for (String tableName : tableNameSet) {
            if (originalSql.contains(tableName)) {
                newSql = originalSql.replaceAll(tableName, tableName + dataTestSqlObject.getSuffix());
                log.info("find a table= " + tableName);
                break;
            }
        }
        //System.out.println(" ->>>newSql: " + newSql);
        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), newSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

        BoundSqlSqlSource sqlSource = new BoundSqlSqlSource(newBoundSql);
        //MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType());
        MappedStatement newMappedStatement = copyFromMappedStatement(mappedStatement,sqlSource);

        invocationArgs[0] = newMappedStatement;


        return invocation.proceed();
    }
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private final BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}

```



#### 总结:

原作者的想法其实稍微有些大胆(对我来说删表是很危险的操作了.哪怕是测试库的.万一哪天sql没改过来把我原来的表给删了呢?)

但这并不妨碍我们学习如何使用`ThreadLocal`和`MybatisInteceptor`

其他更杂的东西我就不说了.可以自己从零手搭一个项目体会,很有意思.笔者在搭建这个项目的时候就遇到过各种大大小小的毛病,比如:

1. `invalid bound statement`(mapperLocation路径多了一个字母)

2. 依旧是`invalid bound statement`(maven插件没有打包xml)

3. `intecptor`拦截器没起作用.(没有注入Spring框

    

    架中)

4. xml执行时显示`Bad result map`(MappedStatement拷贝不完全)

5. 找不到`test/resources`下的文件.(使用getClass().getClassLoader().getResource(filePath))

6. ......

## 2022年7月5日

1. AnyTest新建。引入SpringBoot依赖。
2. 新增/anyTest/testOOM手动测试OOM（~~虽然这种手动调用接口的方式测试很TM低级~~）
