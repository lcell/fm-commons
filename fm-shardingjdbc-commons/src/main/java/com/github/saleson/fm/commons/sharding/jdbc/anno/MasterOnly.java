package com.github.saleson.fm.commons.sharding.jdbc.anno;

import java.lang.annotation.*;

/**
 * @author saleson
 * @date 2020-06-12 17:40
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MasterOnly {
}
