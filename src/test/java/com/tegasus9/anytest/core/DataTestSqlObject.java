package com.tegasus9.anytest.core;

import java.util.List;

/**
 * @author Tegasus9
 * @date 2022/7/9 11:02
 * @description
 */
public class DataTestSqlObject {
    private String suffix;

    private List<String> tableNameSet;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<String> getTableNameSet() {
        return tableNameSet;
    }

    public void setTableNameSet(List<String> tableNameSet) {
        this.tableNameSet = tableNameSet;
    }

    @Override
    public String toString() {
        return "DataTestSqlObject{" +
                "suffix='" + suffix + '\'' +
                ", tableNameSet=" + tableNameSet +
                '}';
    }
}
