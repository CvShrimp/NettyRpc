package com.cvshrimp.spi;

import java.lang.annotation.*;

/**
 * Created by CvShrimp on 2019/12/16.
 *
 * @author wkn
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPI {

	/**
	 * default spi code
	 * @return
	 */
	String value();
}
