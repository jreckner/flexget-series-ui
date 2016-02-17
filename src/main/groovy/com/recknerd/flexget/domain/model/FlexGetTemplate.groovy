package com.recknerd.flexget.domain.model

import static com.fasterxml.jackson.annotation.JsonInclude.Include

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j
import jdk.nashorn.internal.objects.annotations.Getter

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetTemplate {

    @JsonProperty('global')
    FlexGetImmutable deadendGlobal

    @Getter
    getDeadendGlobal() { return deadendGlobal?.data }

    @JsonProperty('tv')
    FlexGetTemplateGroup template

}
