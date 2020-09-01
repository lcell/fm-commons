package com.github.saleson.fm.scanner;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class SpringClassScanner implements ResourceLoaderAware, ClassScanner {

	// 保存过滤规则要排除的注解
	private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
	private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
			this.resourcePatternResolver);


	@Override
	public Set<Class<?>> scan(String... basePackages) {
		return scanByAnnotation(basePackages);
	}

	@Override
	public Set<Class<?>> scanAssignableType(String basePackages, Class<?>... targetType) {
		return scanAssignableType(StringUtils.tokenizeToStringArray(basePackages, ",; \t\n"), targetType);
	}

	@Override
	public Set<Class<?>> scanAssignableType(String[] basePackages, Class<?>... targetType) {
		Set<AssignableTypeFilter> typeFilters = Arrays.stream(targetType)
				.map(AssignableTypeFilter::new)
				.collect(Collectors.toSet());
		return scanFilter(basePackages, typeFilters);
	}

	@Override
	public Set<Class<?>> scanByAnnotation(String basePackages, Class<? extends Annotation>... annotations) {
		return scanByAnnotation(StringUtils.tokenizeToStringArray(basePackages, ",; \t\n"), annotations);
	}

	@Override
	public Set<Class<?>> scanByAnnotation(String[] basePackages, Class<? extends Annotation>... annotations) {
		Set<AnnotationTypeFilter> typeFilters = Arrays.stream(annotations)
				.map(AnnotationTypeFilter::new)
				.collect(Collectors.toSet());
		return scanFilter(basePackages, typeFilters);
	}


	protected Set<Class<?>> scanFilter(String[] basePackages, Iterable<? extends TypeFilter> includeFilters){
		SpringClassScanner cs = new SpringClassScanner();

		for (TypeFilter typeFilter : includeFilters) {
			cs.addIncludeFilter(typeFilter);
		}

		if(this.includeFilters.size()>0){
			this.includeFilters.forEach(cs::addIncludeFilter);
		}

		if(this.excludeFilters.size()>0){
			this.excludeFilters.forEach(cs::addExcludeFilter);
		}

		Set<Class<?>> classes = new HashSet<Class<?>>();
		for (String s : basePackages)
			classes.addAll(cs.doScan(s));
		return classes;
	}


	public final ResourceLoader getResourceLoader() {
		return this.resourcePatternResolver;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	}

	public void addIncludeFilter(TypeFilter includeFilter) {
		this.includeFilters.add(includeFilter);
	}

	public void addExcludeFilter(TypeFilter excludeFilter) {
		this.excludeFilters.add(0, excludeFilter);
	}

	public void resetFilters(boolean useDefaultFilters) {
		this.includeFilters.clear();
		this.excludeFilters.clear();
	}

	public Set<Class<?>> doScan(String basePackage) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ org.springframework.util.ClassUtils.convertClassNameToResourcePath(
							SystemPropertyUtils.resolvePlaceholders(basePackage))
					+ "/**/*.class";
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);

			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					if ((includeFilters.size() == 0 && excludeFilters.size() == 0) || matches(metadataReader)) {
						try {
							classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return classes;
	}

	protected boolean matches(MetadataReader metadataReader) throws IOException {
		for (TypeFilter tf : this.excludeFilters) {
			if (tf.match(metadataReader, this.metadataReaderFactory)) {
				return false;
			}
		}
		for (TypeFilter tf : this.includeFilters) {
			if (tf.match(metadataReader, this.metadataReaderFactory)) {
				return true;
			}
		}
		return false;
	}
}
