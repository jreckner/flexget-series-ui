package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j
import jdk.nashorn.internal.objects.annotations.Getter

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetTemplate {

    @JsonProperty('global')
    FlexGetDeadEnd deadendGlobal

    @Getter
    getDeadendGlobal() { return deadendGlobal.data }

    @JsonProperty('tv')
    FlexGetTemplateGroup template

}
