package com.cvshrimp.client;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by CvShrimp on 2020/1/15.
 *
 * @author wkn
 */
@Component
@EnableConfigurationProperties(ZkProperties.class)
public class ZkClient implements PriorityOrdered {

	private volatile ZooKeeper zooKeeper;

	@Autowired
	private ZkProperties zkProperties;

	private ZkClient() {}

	/**
	 * 单例
	 * @return
	 */
	public ZooKeeper getInstance() {
		if(zooKeeper == null) {
			synchronized(ZooKeeper.class) {
				if(zooKeeper == null) {
					CountDownLatch countDownLatch = new CountDownLatch(1);
					try {
						zooKeeper = new ZooKeeper(zkProperties.getAddress(), zkProperties.getPort(), event -> {
							if(event.getState().equals(Watcher.Event.KeeperState.SyncConnected)) {
								countDownLatch.countDown();
							}
						});
						countDownLatch.await();
						return zooKeeper;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return zooKeeper;
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}
}
