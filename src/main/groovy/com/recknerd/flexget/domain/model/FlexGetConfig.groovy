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
class FlexGetConfig {

    @JsonProperty
    FlexGetTemplate templates

    @JsonProperty
    FlexGetTask tasks

    @JsonProperty
    FlexGetImmutable email

    @Getter
    def getTasks() { return tasks?.tasks }

    @Getter
    def getEmail() { return email?.data }
}
