package com.serviceimpl;

import com.service.IDemoService;

/**
 * Created by wukn on 2017/6/16.
 */
public class NewDemoServiceImpl implements IDemoService {
    public int sum(int a, int b) {
        return a - b;
    }
}
