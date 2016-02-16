/*
 * Copyright 2015 Lexmark International, Inc.  All rights reserved.
 */

package com.recknerd.flexget.configuration

import groovy.util.logging.Slf4j
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 *
 */
@Component
@ConfigurationProperties(prefix = 'network.http')
@Slf4j
class NetworkConfiguration {
    Integer connectionTimeout
    Integer socketTimeout
    Boolean ignoreSSLIssues
}

