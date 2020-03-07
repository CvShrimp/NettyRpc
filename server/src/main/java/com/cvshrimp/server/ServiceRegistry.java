package com.cvshrimp.server;

import com.cvshrimp.client.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;

import static org.apache.zookeeper.Watcher.Event.KeeperState;

import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ZkClient zkClient;

	public void registerService(Class<?> clazz, String data) {
		ZooKeeper zooKeeper = zkClient.getInstance();
		String interfaceName = clazz.getName();
		try {
			createIfNotExisted(zooKeeper, ZK_REGISTER_PATH + "/" + interfaceName, null);
			createIfNotExisted(zooKeeper, ZK_REGISTER_PATH + "/" + interfaceName + "/providers", null);
			createIfNotExisted(zooKeeper, ZK_REGISTER_PATH + "/" + interfaceName + "/providers/" + data, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerRoot() {
		try {
			ZooKeeper zooKeeper = zkClient.getInstance();
			addRootNode(zooKeeper);
		} catch (Exception e) {
			e.printStackTrace();
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

	private void createIfNotExisted(ZooKeeper zooKeeper, String path, String data) {
		try {
			Stat stat = zooKeeper.exists(path, false);
			// 不是null,则说明存在
			if(stat == null) {
				zooKeeper.create(path, data != null ? data.getBytes() : null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
