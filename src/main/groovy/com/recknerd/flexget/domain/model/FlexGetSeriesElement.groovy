package com.recknerd.flexget.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.recknerd.flexget.domain.web.TvShow
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetSeriesElement {

    String title

    FlexGetSeriesElementSettings settings

    FlexGetSeriesElement() {
    }

    FlexGetSeriesElement(String title) {
        this.title = title
    }

    @JsonCreator
    FlexGetSeriesElement(Map showWithSettings) {
        log.debug("showWithSettings: $showWithSettings")
        if (showWithSettings && showWithSettings.keySet().size() > 0) {
            title = showWithSettings.keySet()[0]
        }

        def settingsAsJson = new ObjectMapper().writeValueAsString(showWithSettings[title])
        settings = new ObjectMapper().readValue(settingsAsJson, FlexGetSeriesElementSettings)
    }

    TvShow asTvShow() {
        TvShow tvShow = new TvShow()
        tvShow.with {
            title = this.title
            if (this.settings) {
                exact = this.settings.exact
            }
        }
        return tvShow
    }
}


