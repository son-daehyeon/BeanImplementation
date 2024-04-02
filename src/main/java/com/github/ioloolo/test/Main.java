package com.github.ioloolo.test;

import com.github.ioloolo.BeanImplementation;
import com.github.ioloolo.annotation.Inject;

public class Main {

	@Inject
	private static TestBean testBean;

	public static void main(String[] args) {

		BeanImplementation.init();

		System.out.println(testBean);
	}
}
