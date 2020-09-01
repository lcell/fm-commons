package com.github.saleson.fm.springcloud.topology;

import com.github.saleson.fm.commons.Scaners;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author saleson
 * @date 2019-11-21 13:51
 */
@Slf4j
public class FeignTopologyAnalyzer implements TopologyAnalyzer {

    @Override
    public ServiceDependent analysisDependent(String packagePath) {
        ServiceDependent serviceDependent = new ServiceDependent();
        serviceDependent.setName(getServiceName());

        Set<Class<?>> classes = Scaners.classScaner().scanByAnnotation(packagePath, FeignClient.class);
        classes.forEach(cls -> {
            FeignClient feignClient = AnnotationUtils.findAnnotation(cls, FeignClient.class);
            if (StringUtils.isNotEmpty(feignClient.url())) {
                return;
            }
            serviceDependent.addDependentService(feignClient.name());
        });
        return serviceDependent;
    }


    private String getServiceName() {
        URL bootStartpYamlURL = getBootstrapYamlRUL();
        if(Objects.isNull(bootStartpYamlURL)){
            return null;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> map = null;
        try {
            map = yaml.load(new FileInputStream(bootStartpYamlURL.getFile()));
        } catch (FileNotFoundException e) {
            log.error("", e);
            return null;
        }
        String serviceName = (String)map.get("spring.application.name");
        return serviceName;
    }


    private URL getBootstrapYamlRUL(){
        ClassLoader cl = getClass().getClassLoader();
        URL url = cl.getResource("bootstrap.yaml");
        if(Objects.isNull(url)){
            url = cl.getResource("bootstrap.yml");
        }
        return url;
    }
}
