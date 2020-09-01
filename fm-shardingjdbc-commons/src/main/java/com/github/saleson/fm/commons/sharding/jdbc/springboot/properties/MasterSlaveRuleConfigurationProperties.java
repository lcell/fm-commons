package com.github.saleson.fm.commons.sharding.jdbc.springboot.properties;

import org.apache.shardingsphere.core.yaml.config.masterslave.YamlMasterSlaveRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author saleson
 * @date 2020-06-10 18:01
 */
@ConfigurationProperties(prefix = "spring.shardingsphere.masterslave")
public class MasterSlaveRuleConfigurationProperties extends YamlMasterSlaveRuleConfiguration {
}
