package com.github.saleson.fm.mvc.scanner;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author saleson
 * @date 2020-09-01 16:42
 */
//@RestController
@RequestMapping(value = {"/test"})
public class Resources {

    @RequestMapping(value = {"a","/b"})
    public String test(){
        return "";
    }

    @PostMapping(value = {"/a","/b"})
    public String postTest(){
        return "";
    }
}
