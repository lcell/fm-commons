package com.github.saleson.fm.commons.url;

import org.junit.Test;

public class URLParserTest {


    @Test
    public void test() {
        try {

            System.err.println(
                    URLParser.fromURL("https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI")
                            .compile().getParameter("gs_l"));

            System.out.println(
                    URLParser.fromURL("https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI")
                            .getParameter("q"));

            System.out.println(
                    URLParser.fromURL("https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI")
                            .compile().getParameter("q"));

            System.out.println(
                    URLParser.fromURL("https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI#test-label")
                            .compile().setParameter("q", "tweaked").toURL());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
