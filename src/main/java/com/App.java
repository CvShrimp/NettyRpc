package com;

import com.service.IDemoService;

/**
 * Created by wukn on 2017/6/15.
 */
public class App {

    public static void main(String[] args) {
        JDKDynamicService<IBasicService> proxy = new JDKDynamicService<IBasicService>(IBasicService.class);
        IBasicService service = proxy.get();
        int[] a = {3,6,8,9,10,11};
        System.out.println("result:" + service.binarySearch(a,11));

        JDKDynamicService<IDemoService> proxy1 = new JDKDynamicService<IDemoService>(IDemoService.class);
        IDemoService service1 = proxy1.get();
        System.out.println("result:" + service1.sum(6,6));
    }
}
