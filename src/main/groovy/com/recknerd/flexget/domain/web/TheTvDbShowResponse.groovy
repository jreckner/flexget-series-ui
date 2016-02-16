package com.recknerd.flexget.domain.web

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import groovy.util.logging.Slf4j

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = 'Data')
@Slf4j
class TheTvDbShowResponse {

    @JacksonXmlProperty(localName = 'Series')
    @JacksonXmlElementWrapper(useWrapping = false)
    List<TheTvDbShowSeriesResponse> series
}
