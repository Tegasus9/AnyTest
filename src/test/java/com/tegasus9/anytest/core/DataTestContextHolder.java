package com.tegasus9.anytest.core;

/**
 * @author Tegasus9
 * @date 2022/7/9 11:02
 * @description
 */
public class DataTestContextHolder {
    private static ThreadLocal<DataTestSqlObject> threadLocal = new ThreadLocal<>();

    public static DataTestSqlObject getDataTestSqlObject() {
        return threadLocal.get();
    }

    public static void setDataTestSqlObject(DataTestSqlObject dataTestSqlObject) {
        threadLocal.set(dataTestSqlObject);
    }

    public static void clear() {
        threadLocal.remove();
    }
}
