package com.tegasus9.anytest.common.entity;

import com.tegasus9.anytest.common.constant.ResultConstant;
import lombok.Data;

/**
 * @author XiongYiGe
 * @date 2022/6/20
 * @description
 */
@Data
public class BaseResponse<T> {
    private String code;
    private String message;
    private T data;

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> BaseResponse<T> success(){
        return new BaseResponse<>(ResultConstant.SUCCESS.getCode(), ResultConstant.SUCCESS.getMsg());
    }

    public static <T> BaseResponse<T> success(String message){
        return new BaseResponse<>(ResultConstant.SUCCESS.getCode(), message);
    }
    public static <T> BaseResponse<T> fail(){
        return new BaseResponse<>(ResultConstant.FAIL.getCode(), ResultConstant.FAIL.getMsg());
    }
    public static <T> BaseResponse<T> fail(String message){
        return new BaseResponse<>(ResultConstant.FAIL.getCode(), message);
    }
}
