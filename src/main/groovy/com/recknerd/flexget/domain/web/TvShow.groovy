package com.recknerd.flexget.domain.web

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j

@Slf4j
class TvShow {

    @JsonProperty
    String title

    @JsonProperty
    boolean exact
}
