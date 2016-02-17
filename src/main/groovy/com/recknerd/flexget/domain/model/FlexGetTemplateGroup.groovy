package com.recknerd.flexget.domain.model

import static com.fasterxml.jackson.annotation.JsonInclude.Include

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j
import jdk.nashorn.internal.objects.annotations.Getter

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetTemplateGroup {

    @JsonProperty
    boolean thetvdb_lookup

    @JsonProperty
    boolean verify_ssl_certificates

    @JsonProperty
    String quality

    @JsonProperty
    FlexGetSeries series

    @JsonProperty('exists_series')
    FlexGetImmutable deadendExistsSeries

    @JsonProperty('set')
    FlexGetImmutable deadendSet

    @JsonProperty('transmission')
    FlexGetImmutable deadendTransmission

    @JsonProperty('clean_transmission')
    FlexGetImmutable deadendCleanTransmission

    @Getter
    getDeadendExistsSeries() { return deadendExistsSeries?.data }

    @Getter
    getDeadendSet() { return deadendSet?.data }

    @Getter
    getDeadendTransmission() { return deadendTransmission?.data }

    @Getter
    getDeadendCleanTransmission() { return deadendCleanTransmission?.data }

}
