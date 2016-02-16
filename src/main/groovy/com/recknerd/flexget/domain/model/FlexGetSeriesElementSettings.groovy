package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetSeriesElementSettings {

    @JsonProperty
    boolean exact

    @JsonProperty
    String quality

}
