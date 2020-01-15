package com.cvshrimp.server;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by CvShrimp on 2020/1/15.
 *
 * @author wkn
 */
public class ZkClient {

	private static volatile ZooKeeper zooKeeper;

	private ZkClient() {}

	/**
	 * 单例
	 * @return
	 */
	public static ZooKeeper getInstance() {
		if(zooKeeper == null) {
			synchronized(ZooKeeper.class) {
				if(zooKeeper == null) {
					CountDownLatch countDownLatch = new CountDownLatch(1);
					try {
						zooKeeper = new ZooKeeper("127.0.0.1", 2181, event -> {
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
}
