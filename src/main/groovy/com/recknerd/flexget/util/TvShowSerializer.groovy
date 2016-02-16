package com.recknerd.flexget.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.recknerd.flexget.domain.model.FlexGetSeriesElement
import com.recknerd.flexget.domain.model.FlexGetSeriesElementSettings
import groovy.util.logging.Slf4j

@Slf4j
class TvShowSerializer extends JsonSerializer<FlexGetSeriesElement> {

    @Override
    void serialize(FlexGetSeriesElement value, JsonGenerator jgen, SerializerProvider provider) {

        def ygen = (YAMLGenerator) jgen

        if (value.settings) {
            ygen.writeStartObject()
            ygen.writeObjectFieldStart(value.title)

            def availableSettings = FlexGetSeriesElementSettings.metaClass.properties*.name
            availableSettings.remove('class')

            availableSettings.each {
                if (value.settings[it]) {
                    log.debug("Found the setting: $it")
                    if (value.settings[it] instanceof Boolean ) {
                        ygen.writeBooleanField(it, value.settings[it])
                    }
                    if (value.settings[it] instanceof String ) {
                        ygen.writeStringField(it, value.settings[it])
                    }
                    if (value.settings[it] instanceof Integer ) {
                        ygen.writeNumberField(it, value.settings[it])
                    }
                }
            }

            ygen.writeEndObject()
            ygen.writeEndObject()
        }
        else {
            ygen.writeString(value.title)
        }
    }
}
