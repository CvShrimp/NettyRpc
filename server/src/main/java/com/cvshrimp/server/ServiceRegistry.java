package com.cvshrimp.server;

import com.cvshrimp.client.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Component
public class ServiceRegistry {

	private static final String ZK_REGISTER_PATH = "/rpc";

	private static Logger log = LoggerFactory.getLogger(ServiceRegistry.class);

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
			log.error(String.format("Failed to register the service %s", interfaceName), e);
		}
	}

	public void registerRoot() {
		try {
			ZooKeeper zooKeeper = zkClient.getInstance();
			addRootNode(zooKeeper);
		} catch (Exception e) {
			log.error("Failed to register the root node", e);
		}
	}

	private void addRootNode(ZooKeeper zooKeeper) {
		try {
			Stat stat = zooKeeper.exists(ZK_REGISTER_PATH, false);
			// if it is not null, it should be existed. skip to handle
			if(stat == null) {
				zooKeeper.create(ZK_REGISTER_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
			}
		} catch (KeeperException e) {
			log.error("Failed to add the root node", e);
		} catch (InterruptedException e) {
			log.error("Failed to add the root node", e);
		}
	}

	private void createIfNotExisted(ZooKeeper zooKeeper, String path, String data) {
		try {
			Stat stat = zooKeeper.exists(path, false);
			// if it is not null, it should be existed. skip to handle
			if(stat == null) {
				zooKeeper.create(path, data != null ? data.getBytes() : null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			log.error(String.format("Failed to add the path %s", path), e);
		}
	}
}
