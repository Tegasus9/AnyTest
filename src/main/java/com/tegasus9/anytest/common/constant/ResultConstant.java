package com.tegasus9.anytest.common.constant;

/**
 * @author XiongYiGe
 * @date 2022/7/5
 * @description
 */
public enum ResultConstant {
    SUCCESS("success", "成功"),
    FAIL("fail", "失败"),
    ;
    private final String code;
    private final String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResultConstant(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
