package com.serviceimpl;

import com.IBasicService;

/**
 * Created by wukn on 2017/6/16.
 */
public class BasicServiceImpl implements IBasicService{

    public int binarySearch(int[] a, int des) {
        int low = 0;
        int high = a.length - 1;
        int mid = (low + high)/2;
        while(high >= low) {
            if(des > a[mid]){
                low = mid + 1;
            }else if(des < a[mid]){
                high = mid - 1;
            }else{
                return mid;
            }
            mid = (low + high)/2;
        }
        return -1;
    }

}
