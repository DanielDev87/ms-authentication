package co.com.bancolombia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.r2dbc",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".+Adapter$")
        },
        useDefaultFilters = false)
public class AdapterConfig {
}