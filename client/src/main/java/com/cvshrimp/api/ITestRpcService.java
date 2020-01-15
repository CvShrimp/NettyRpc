package com.cvshrimp.api;

import com.cvshrimp.client.ReferenceRpc;

/**
 * Created by wukn on 2017/6/15.
 */
@ReferenceRpc
public interface ITestRpcService {

    int multiply(int a, int b);
}
