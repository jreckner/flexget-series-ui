package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.util.logging.Slf4j
import jdk.nashorn.internal.objects.annotations.Getter

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetConfig {

    @JsonProperty
    FlexGetTemplate templates

    @JsonProperty
    FlexGetTask tasks

    @Getter
    def getTasks() { return tasks.tasks }
}
