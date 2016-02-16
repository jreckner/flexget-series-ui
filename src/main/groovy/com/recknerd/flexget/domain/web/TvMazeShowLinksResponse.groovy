package com.recknerd.flexget.domain.web

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class TvMazeShowLinksResponse {

    @JsonProperty
    Map<String, String> self

    @JsonProperty
    Map<String, String> nextepisode
}
