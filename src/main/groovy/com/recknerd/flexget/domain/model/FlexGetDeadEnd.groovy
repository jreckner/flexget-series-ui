package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetDeadEnd {

    def data

    @JsonCreator
    FlexGetDeadEnd(Map data) {
        log.debug("deadend data: $data")
        this.data = data
    }
}
