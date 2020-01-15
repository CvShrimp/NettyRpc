package com.cvshrimp.server;

import com.cvshrimp.api.ITestRpcService;

/**
 * Created by CvShrimp on 2019/11/5.
 *
 * @author wkn
 */
@RpcService
public class TestRpcServiceImpl implements ITestRpcService {

	@Override
	public int multiply(int a, int b) {
		return a * b;
	}
}
