package com.github.saleson.fm.springcloud.topology;

import lombok.Data;

import java.util.*;

/**
 * @author saleson
 * @date 2019-11-21 13:45
 */
@Data
public class ServiceDependent {
    private String name;
    private Set<String> dependents;


    public Set<String> getDependents() {
        if(Objects.isNull(dependents)){
            dependents = new HashSet<>();
        }
        return dependents;
    }

    public void addDependentService(String dependentService){
        getDependents().add(dependentService);
    }
}
