package com.cvshrimp.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by CvShrimp on 2020/1/15.
 *
 * @author wkn
 */
@Data
@ConfigurationProperties(prefix = "registry")
public class ZkProperties {

	private String address;

	private Integer port;
}
