package com.github.saleson.fm.scanner;

import com.github.saleson.fm.enums.ClassType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-09-01 14:29
 */
public class SpringMVCEndPointScanner implements EndPointScanner {

    private ClassScanner classScanner;

    public SpringMVCEndPointScanner() {
        this(new SpringClassScanner());
    }

    public SpringMVCEndPointScanner(ClassType classType, Class<?>... classes) {
        this(new SpringClassScanner());
        initSpringScannerTypeFilter(classType, classes);
    }

    public SpringMVCEndPointScanner(ClassScanner classScanner) {
        this.classScanner = classScanner;
    }

    @Override
    public Set<EndPoint> scan(String... basePackages) {
        Set<Class<?>> classes = getScanClasses(basePackages);
        Set<EndPoint> endPoints = new HashSet<>(classes.size() * 2);

        for (Class<?> aClass : classes) {
            Set<EndPoint> eps = getClassEndPoints(aClass);
            endPoints.addAll(eps);
        }

        return endPoints;
    }

    protected Set<Class<?>> getScanClasses(String... basePackages) {
        return classScanner.scan(basePackages);
    }

    private Set<EndPoint> getClassEndPoints(Class<?> epCls) {
        Set<EndPoint> methodEps = getAllMethodEndPoints(epCls);
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(epCls, RequestMapping.class);
        if (Objects.isNull(requestMapping) || ArrayUtils.isEmpty(requestMapping.value())) {
            return methodEps;
        }

        Set<EndPoint> clsEps = createEndPoints(requestMapping.value(), "");
//        Set<EndPoint> allEps = new HashSet<>(methodEps.size() * clsEps.size());
//        for (EndPoint clsEp : clsEps) {
//            String prefixPath = clsEp.getPath();
//            if(StringUtils.isBlank(prefixPath)){
//                continue;
//            }
//            for (EndPoint methodEp : methodEps) {
//                String path = methodEp.getPath();
//                if(!StringUtils.startsWith(path, "/")){
//                    path = "/" + path;
//                }
//                EndPoint ep = EndPoint.of(prefixPath + path, methodEp.getMethod());
//                allEps.add(ep);
//            }
//        }
//        return allEps;
        return clsEps.stream()
                .flatMap(clsEp -> {
                    String prefixPath = clsEp.getPath();
                    if (StringUtils.isBlank(prefixPath)) {
                        return null;
                    }

                    return methodEps.stream().map(methodEp -> {
                        String path = methodEp.getPath();
                        if (!StringUtils.startsWith(path, "/")) {
                            path = "/" + path;
                        }
                        return EndPoint.of(prefixPath + path, methodEp.getMethod());
                    });
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<EndPoint> getAllMethodEndPoints(Class<?> epCls) {
        Method[] methods = epCls.getMethods();
        Set<EndPoint> endPoints = new HashSet<>(methods.length * 2);
        for (Method method : epCls.getMethods()) {
            Set<EndPoint> eps = getMethodEndPoints(method);
            endPoints.addAll(eps);
        }
        return endPoints;
    }


    private Set<EndPoint> getMethodEndPoints(Method method) {
        PostMapping postMapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
        if (Objects.nonNull(postMapping)) {
            return analysisEndPoints(postMapping);
        }

        GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
        if (Objects.nonNull(getMapping)) {
            return analysisEndPoints(getMapping);
        }

        PutMapping putMapping = AnnotationUtils.getAnnotation(method, PutMapping.class);
        if (Objects.nonNull(putMapping)) {
            return analysisEndPoints(putMapping);
        }

        PatchMapping patchMapping = AnnotationUtils.getAnnotation(method, PatchMapping.class);
        if (Objects.nonNull(patchMapping)) {
            return analysisEndPoints(patchMapping);
        }

        DeleteMapping deleteMapping = AnnotationUtils.getAnnotation(method, DeleteMapping.class);
        if (Objects.nonNull(deleteMapping)) {
            return analysisEndPoints(deleteMapping);
        }

        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return Collections.emptySet();
        }
        return analysisEndPoints(requestMapping);
    }

    private Set<EndPoint> analysisEndPoints(PatchMapping mapping) {
        return createEndPoints(mapping.value(), "PATCH");
    }

    private Set<EndPoint> analysisEndPoints(GetMapping mapping) {
        return createEndPoints(mapping.value(), "GET");
    }

    private Set<EndPoint> analysisEndPoints(PostMapping mapping) {
        return createEndPoints(mapping.value(), "POST");
    }

    private Set<EndPoint> analysisEndPoints(PutMapping mapping) {
        return createEndPoints(mapping.value(), "PUT");
    }

    private Set<EndPoint> analysisEndPoints(DeleteMapping deleteMapping) {
        return createEndPoints(deleteMapping.value(), "DELETE");
    }

    private Set<EndPoint> analysisEndPoints(RequestMapping requestMapping) {
        return createEndPoints(requestMapping.value(), requestMapping.method());
    }

    private Set<EndPoint> createEndPoints(String[] urls, RequestMethod[] requestMethods) {
        if (ArrayUtils.isEmpty(requestMethods)) {
            return createEndPoints(urls, "");
        }
        Set<EndPoint> set = new HashSet<>(urls.length * requestMethods.length);
        for (RequestMethod requestMethod : requestMethods) {
            set.addAll(createEndPoints(urls, requestMethod.name()));
        }
        return set;
    }


    private Set<EndPoint> createEndPoints(String[] urls, String method) {
        Set<EndPoint> set = new HashSet<>(urls.length);
        for (String url : urls) {
            set.add(EndPoint.of(url, method));
        }
        return set;
    }


    private void initSpringScannerTypeFilter(ClassType classType, Class<?>... classes) {
        SpringClassScanner springClassScanner = (SpringClassScanner) this.classScanner;

        if(Objects.equals(classType, ClassType.ANNOTATION)){
            for (Class<?> aClass : classes) {
                springClassScanner.addIncludeFilter(new AnnotationTypeFilter((Class<? extends Annotation>) aClass));
            }
        }else{
            for (Class<?> aClass : classes) {
                springClassScanner.addIncludeFilter(new AssignableTypeFilter(aClass));
            }
        }

    }

}
