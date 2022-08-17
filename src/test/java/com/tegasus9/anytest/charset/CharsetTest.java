package com.tegasus9.anytest.charset;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author XiongYiGe
 * @date 2022/7/25
 * @description
 */
 class CharsetTest {
    @Test
     void charsetConvert() throws UnsupportedEncodingException {
        String str = "中文";
        System.out.println(str);
        String convertString = CharsetUtil.convert(str, CharsetUtil.CHARSET_UTF_8, CharsetUtil.CHARSET_GBK);
        System.out.println("charset = " + convertString);
        String gbkString = new String(str.getBytes(), "GBK");
        System.out.println("gbkString = " + gbkString);
        String GBKGarbled = new String(gbkString.getBytes(), StandardCharsets.UTF_8);
        System.out.println("GBKGarbled = " + GBKGarbled);
        System.out.println(new String(str.getBytes(), StandardCharsets.UTF_8));
    }
}
