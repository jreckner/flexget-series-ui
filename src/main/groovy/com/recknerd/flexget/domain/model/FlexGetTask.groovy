package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetTask {

    def tasks

    @JsonCreator
    FlexGetTask(Map tasks) {
        log.debug("tasks: $tasks")
        this.tasks = tasks
    }
}
