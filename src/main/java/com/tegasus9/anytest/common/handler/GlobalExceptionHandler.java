package com.tegasus9.anytest.common.handler;

import com.tegasus9.anytest.common.entity.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author XiongYiGe
 * @date 2022/7/5
 * @description
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> handleException(Exception e){
        log.error("异常信息：{}",e.getMessage());
        return BaseResponse.fail();
    }
}
