package com.indracompany.walle.application.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties("walle.cors")
public class CorsProperties {

    private String allowedOrigin;

    public String getAllowedOrigin() {
        if(allowedOrigin == null) {
            setAllowedOrigin("*");
        }
        return allowedOrigin;
    }

    public void setAllowedOrigin(String allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

}
