package com.recknerd.flexget.domain.web

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class TheTvDbShowSeriesResponse {

    @JacksonXmlProperty()
    int id

    @JacksonXmlProperty()
    int seriesId

    @JacksonXmlProperty(localName = 'SeriesName')
    String seriesName

    @JacksonXmlProperty(localName = 'IMDB_ID')
    String imdbId

    @Override
    String toString() {
        return "$id: $seriesName - IMDB#$imdbId"
    }
}
