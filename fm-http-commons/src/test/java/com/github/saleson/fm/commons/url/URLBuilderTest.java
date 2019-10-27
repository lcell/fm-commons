package com.github.saleson.fm.commons.url;

import org.junit.Test;

public class URLBuilderTest {

    @Test
    public void test(){
        URLBuilder urlBuilder = new URLBuilder();
        urlBuilder.
                appendPath("https://weixin.kaishustory.com").
                appendParam("key1", "value1").
                appendParam("key2", "value2").appendParamEncode("ekey3", "{$#你好}").appendLabel("label");
        System.out.println(urlBuilder.toString());
    }

}
