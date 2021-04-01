package com.hui.base.springcloud.netty.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * ID生成器
 */
@Component
public class IdBuilder {


	/**
	 * 生成32位UUID，没有“-”
	 *
	 * @return String 32位字符
	 */
	public static String getID() {
		UUID id = UUID.randomUUID();
		return id.toString().replaceAll("-", "");
	}
}