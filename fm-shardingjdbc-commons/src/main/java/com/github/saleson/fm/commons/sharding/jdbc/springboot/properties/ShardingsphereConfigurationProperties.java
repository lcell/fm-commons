package com.github.saleson.fm.commons.sharding.jdbc.springboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author saleson
 * @date 2020-06-10 18:00
 */
@ConfigurationProperties(prefix = "spring.shardingsphere")
@Data
public class ShardingsphereConfigurationProperties {
    private Properties props = new Properties();
}
