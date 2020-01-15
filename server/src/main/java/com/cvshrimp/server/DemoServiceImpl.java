package com.cvshrimp.server;

import com.cvshrimp.api.IDemoService;

/**
 * Created by wukn on 2017/6/15.
 */
@RpcService
public class DemoServiceImpl implements IDemoService {

    @Override
    public int sum(int a, int b) {
        return a + b;
    }

}
