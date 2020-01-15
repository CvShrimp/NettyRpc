package com.cvshrimp.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Documented
@Inherited
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

}
