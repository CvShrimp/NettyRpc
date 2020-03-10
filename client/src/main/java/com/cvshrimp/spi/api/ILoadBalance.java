package com.cvshrimp.spi.api;

import com.cvshrimp.client.Invoker;
import com.cvshrimp.spi.SPI;

import java.util.List;

/**
 * Created by CvShrimp on 2020/3/10
 *
 * @author wkn
 */
@SPI("random")
public interface ILoadBalance {

    Invoker loadBalance(List<String> addressList);
}
