package com.hui.base.springcloud.order.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 动态配置
 */
@ConfigurationProperties(prefix = "dynamic-config")
@Component
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DynamicConfig {
    private String name;

    private String address;
}
