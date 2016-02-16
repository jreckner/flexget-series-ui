package com.recknerd.flexget.domain.web

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class TvMazeShowResponse {

    @JsonProperty
    int id

    @JsonProperty
    String url

    @JsonProperty
    String name

    @JsonProperty
    String language

    @JsonProperty
    String status

    @JsonProperty
    TvMazeShowScheduleResponse schedule

    @JsonProperty
    TvMazeShowRatingResponse rating

    @JsonProperty
    TvMazeShowExternalsResponse externals
}
