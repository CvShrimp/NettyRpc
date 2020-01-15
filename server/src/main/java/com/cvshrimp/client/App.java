package com.cvshrimp.client;

import com.cvshrimp.api.IDemoService;

/**
 * Created by wukn on 2017/6/15.
 */
public class App {

    public static void main(String[] args) {
        JDKDynamicService<IDemoService> proxy = new JDKDynamicService<IDemoService>(IDemoService.class);
        IDemoService service = proxy.get();
        System.out.println("result:" + service.sum(6,6));
    }
}
