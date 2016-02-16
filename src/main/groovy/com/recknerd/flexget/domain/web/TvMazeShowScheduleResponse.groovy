package com.recknerd.flexget.domain.web

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j

@Slf4j
class TvMazeShowScheduleResponse {

    @JsonProperty
    String time

    @JsonProperty
    List<String> days
}
