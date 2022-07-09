package com.tegasus9.anytest.infrastructrue.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author Tegasus9
 * @date 2022/7/9 11:29
 * @description
 */
public interface SqlMapper {
    /**
     * 直接执行SQL
     * @param sql
     */
    void directDoSql(@Param("sql") String sql);
}
