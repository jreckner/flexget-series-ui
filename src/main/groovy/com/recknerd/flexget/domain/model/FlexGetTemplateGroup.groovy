package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j
import jdk.nashorn.internal.objects.annotations.Getter

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetTemplateGroup {

    @JsonProperty
    boolean thetvdb_lookup

    @JsonProperty
    boolean verify_ssl_certificates

    @JsonProperty
    FlexGetSeries series

    @JsonProperty('exists_series')
    FlexGetDeadEnd deadendExistsSeries

    @JsonProperty('set')
    FlexGetDeadEnd deadendSet

    @Getter
    getDeadendExistsSeries() { return deadendExistsSeries.data }

    @Getter
    getDeadendSet() { return deadendSet ? deadendSet.data : null }

}
