package com.cvshrimp.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Component
public class RpcScanner implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		ClassPathRpcScanner scanner = new ClassPathRpcScanner(registry);

		scanner.setAnnotationClass(ReferenceRpc.class);
		scanner.registerFilters();

		scanner.scan(StringUtils.tokenizeToStringArray("com.cvshrimp.api"
				, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}
}
