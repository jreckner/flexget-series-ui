package com.recknerd.flexget.domain.model

import static com.fasterxml.jackson.annotation.JsonInclude.Include

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.recknerd.flexget.util.TvShowSerializer
import groovy.util.logging.Slf4j
import jdk.nashorn.internal.objects.annotations.Getter

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
class FlexGetSeries {

    @JsonProperty('settings')
    FlexGetImmutable settings

    @Getter
    getSettings() { return settings?.data }

    @JsonProperty
    @JsonSerialize(contentUsing=TvShowSerializer)
    List<FlexGetSeriesElement> hdGroup

    @Getter
    getHdGroup() { return hdGroup ? hdGroup.sort { it.title } : (hdGroup = []) }

    @JsonProperty
    @JsonSerialize(contentUsing=TvShowSerializer)
    List<FlexGetSeriesElement> stdGroup

    @Getter
    getStdGroup() { return stdGroup ? stdGroup.sort { it.title } : (stdGroup = []) }
}
