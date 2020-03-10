package com.cvshrimp.spi;

/**
 * Created by CvShrimp on 2019/12/16.
 *
 * @author wkn
 */
public class Holder<T> {

	private volatile T value;

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
}
