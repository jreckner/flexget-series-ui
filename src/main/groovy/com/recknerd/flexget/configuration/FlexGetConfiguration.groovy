package com.recknerd.flexget.configuration

import groovy.util.logging.Slf4j
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = 'flexget')
@Slf4j
class FlexGetConfiguration {
    String config
}
