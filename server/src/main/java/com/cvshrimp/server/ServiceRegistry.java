package com.cvshrimp.server;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;

import static org.apache.zookeeper.Watcher.Event.KeeperState;

import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Component
public class ServiceRegistry {

	private static final String ZK_REGISTER_PATH = "/rpc";

	public void register(String data) {
		if(StringUtils.isNotEmpty(data)) {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			try {
				ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1", 2181, event -> {
					if(event.getState().equals(KeeperState.SyncConnected)) {
						countDownLatch.countDown();
					}
				});
				countDownLatch.await();

				addRootNode(zooKeeper);
				createNode(zooKeeper, data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addRootNode(ZooKeeper zooKeeper) {
		try {
			Stat stat = zooKeeper.exists(ZK_REGISTER_PATH, false);
			// 不是null,则说明存在
			if(stat == null) {
				zooKeeper.create(ZK_REGISTER_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void createNode(ZooKeeper zooKeeper, String data) {
		try {
			zooKeeper.create(ZK_REGISTER_PATH + "/provider", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
