package com.tegasus9.anytest.core;

/**
 * @author Tegasus9
 * @date 2022/7/9 11:20
 * @description 用来设置数据源的上下文
 * 这里原作者应该对数据源切换有考虑，但实际文章发布时没有使用。
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> DATA_SOURCE = new ThreadLocal<>();

    public static String getDB(){
        return DATA_SOURCE.get();
    }

    public static void setDB(String db){
        DATA_SOURCE.set(db);
    }

    public static void clear(){
        DATA_SOURCE.remove();
    }
}
