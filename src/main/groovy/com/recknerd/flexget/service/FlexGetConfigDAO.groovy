package com.recknerd.flexget.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.recknerd.flexget.configuration.FlexGetConfiguration
import com.recknerd.flexget.domain.model.FlexGetConfig

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Slf4j
class FlexGetConfigDAO {

    @Autowired
    FlexGetConfiguration flexGetConfiguration

    FlexGetConfig flexGetConfig

    YAMLFactory yamlFactory = new YAMLFactory()

    void init() {

        def config = new File(flexGetConfiguration.config)
        if (!config.exists()) {
            log.error("Missing config: $config.absolutePath")
        }

        if (flexGetConfig) {
            return
        }
        load()
    }

    def load() {
        ObjectMapper mapper = new ObjectMapper(yamlFactory)
        flexGetConfig = mapper.readValue(new File(flexGetConfiguration.config), FlexGetConfig)
    }

    def write() {
        new File(flexGetConfiguration.config).withOutputStream { fos ->
            yamlFactory.createGenerator(fos).writeObject(flexGetConfig)
        }
    }

}
