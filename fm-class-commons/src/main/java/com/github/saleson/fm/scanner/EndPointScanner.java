package com.github.saleson.fm.scanner;

import lombok.*;

import java.util.Set;

/**
 * @author saleson
 * @date 2020-09-01 14:25
 */
public interface EndPointScanner {


    Set<EndPoint> scan(String... basePackages);



    @Data
    @Builder
    @AllArgsConstructor
    public static class EndPoint{
        private String path;
        private String method;



        public static EndPoint of(String url, String method){
            return EndPoint.builder()
                    .path(url)
                    .method(method)
                    .build();
        }
    }
}
