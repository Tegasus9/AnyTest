package com.tegasus9.anytest.charset;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author XiongYiGe
 * @date 2022/7/25
 * @description
 */
 class CharsetTest {
    @Test
     void charsetConvert() throws UnsupportedEncodingException {
        String str = "中国";
        System.out.println(str);
        String convertString = CharsetUtil.convert(str, CharsetUtil.CHARSET_UTF_8, CharsetUtil.CHARSET_GBK);
        System.out.println("charset = " + convertString);
        String gbkString = new String(str.getBytes(), "GBK");
        System.out.println("gbkString = " + gbkString);
        String gbkluanma = new String(gbkString.getBytes(), "UTF-8");
        System.out.println("gbkluanma = " + gbkluanma);
        System.out.println(new String(str.getBytes(), "UTF-8"));
    }
}
