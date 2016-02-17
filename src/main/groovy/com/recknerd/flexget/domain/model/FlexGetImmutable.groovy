package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetImmutable {

    def data

    @JsonCreator
    FlexGetImmutable(Map data) {
        log.debug("immutable data: $data")
        this.data = data
    }
}
