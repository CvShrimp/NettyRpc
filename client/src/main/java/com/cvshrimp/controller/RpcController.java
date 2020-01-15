package com.cvshrimp.controller;

import com.cvshrimp.api.IDemoService;
import com.cvshrimp.api.ITestRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by CvShrimp on 2019/11/5.
 *
 * @author wkn
 */
@RestController
public class RpcController {

	@Autowired
	private IDemoService demoService;

	@Autowired
	private ITestRpcService testRpcService;

	@RequestMapping("/rpc/{a}/{b}")
	public int testRpc(@PathVariable int a, @PathVariable int b) {
		try {
			return demoService.sum(a, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@RequestMapping("/rpc/mul/{a}/{b}")
	public int testMulRpc(@PathVariable int a, @PathVariable int b) {
		try {
			return testRpcService.multiply(a, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
