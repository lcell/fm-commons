package com.github.saleson.fm.springcloud.topology;

/**
 * @author saleson
 * @date 2019-11-21 13:45
 */
public interface TopologyAnalyzer {

    ServiceDependent analysisDependent(String packagePath);

}
