package com.github.saleson.fm.commons.sharding.jdbc;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.hint.HintManager;
import org.apache.shardingsphere.core.config.inline.InlineExpressionParser;
import org.apache.shardingsphere.core.exception.ShardingException;
import org.apache.shardingsphere.core.yaml.config.masterslave.YamlMasterSlaveRuleConfiguration;
import org.apache.shardingsphere.core.yaml.swapper.impl.MasterSlaveRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.spring.boot.datasource.DataSourcePropertiesSetter;
import org.apache.shardingsphere.spring.boot.datasource.DataSourcePropertiesSetterHolder;
import org.apache.shardingsphere.spring.boot.util.DataSourceUtil;
import org.apache.shardingsphere.spring.boot.util.PropertyUtil;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author saleson
 * @date 2020-06-10 17:08
 */
public class ShardingJdbcHelper {

    private static final String propertiesPrefix = "spring.shardingsphere.datasource.";
    private static final String jndiName = "jndi-name";


    public static void setMasterRouteOnly(){
        HintManager.getInstance().setMasterRouteOnly();
    }


    public static void clearMasterRouteOnly(){
        HintManager.clear();
    }



    public static DataSource createMasterSlaveDataSource(Map<String, DataSource> dataSourceMap, YamlMasterSlaveRuleConfiguration yamlConfiguration, Properties props) throws SQLException {
        return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, new MasterSlaveRuleConfigurationYamlSwapper().swap(yamlConfiguration), props);
    }

    public static Map<String, DataSource> parseDataSourceFromEnvironment(Environment environment){
        return parseDataSourceFromEnvironment(propertiesPrefix, environment);
    }

    public static Map<String, DataSource> parseDataSourceFromEnvironment(String propertyPrefix, Environment environment){
        Map<String, DataSource> dataSources = new HashMap<>();
        String prefix = StringUtils.endsWith(propertyPrefix, ".") ? propertyPrefix : propertyPrefix + ".";
        for (String each : getDataSourceNames(environment, prefix)) {
            try {
                dataSources.put(each, getDataSource(environment, prefix, each));
            } catch (final ReflectiveOperationException ex) {
                throw new ShardingException("Can't find datasource type!", ex);
            } catch (final NamingException namingEx) {
                throw new ShardingException("Can't find JNDI datasource!", namingEx);
            }
        }
        return dataSources;
    }



    private static List<String> getDataSourceNames(final Environment environment, final String prefix) {
        StandardEnvironment standardEnv = (StandardEnvironment) environment;
        standardEnv.setIgnoreUnresolvableNestedPlaceholders(true);
        return null == standardEnv.getProperty(prefix + "name")
                ? new InlineExpressionParser(standardEnv.getProperty(prefix + "names")).splitAndEvaluate() : Collections.singletonList(standardEnv.getProperty(prefix + "name"));
    }

    private static DataSource getDataSource(final Environment environment, final String prefix, final String dataSourceName) throws ReflectiveOperationException, NamingException {
        Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + dataSourceName.trim(), Map.class);
        Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
        if (dataSourceProps.containsKey(jndiName)) {
            return getJndiDataSource(dataSourceProps.get(jndiName).toString());
        }
        DataSource result = DataSourceUtil.getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
        Optional<DataSourcePropertiesSetter> dataSourcePropertiesSetter = DataSourcePropertiesSetterHolder.getDataSourcePropertiesSetterByType(dataSourceProps.get("type").toString());
        if (dataSourcePropertiesSetter.isPresent()) {
            dataSourcePropertiesSetter.get().propertiesSet(environment, prefix, dataSourceName, result);
        }
        return result;
    }

    private static DataSource getJndiDataSource(final String jndiName) throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setResourceRef(true);
        bean.setJndiName(jndiName);
        bean.setProxyInterface(DataSource.class);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }
}
