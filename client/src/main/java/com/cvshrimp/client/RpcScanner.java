package com.cvshrimp.client;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Component
public class RpcScanner implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// 查找zk注册信息
		findRegistry();

		ClassPathRpcScanner scanner = new ClassPathRpcScanner(registry);

		scanner.setAnnotationClass(ReferenceRpc.class);
		scanner.registerFilters();

		scanner.scan(StringUtils.tokenizeToStringArray("com.cvshrimp.api"
				, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	private void findRegistry() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1", 2181, event -> {
				if(event.getState().equals(Watcher.Event.KeeperState.SyncConnected)) {
					countDownLatch.countDown();
				}
			});
			countDownLatch.await();

			byte[] content = zooKeeper.getData("/rpc/provider", true, new Stat());
			String address = new String(content);
			String[] infoArray = address.split(":");
			RpcFactory.setAddress(infoArray[0]);
			RpcFactory.setPort(Integer.valueOf(infoArray[1]));
			System.out.println(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
