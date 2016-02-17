package com.recknerd.flexget.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.recknerd.flexget.configuration.FlexGetConfiguration
import com.recknerd.flexget.domain.model.FlexGetConfig
import groovy.util.logging.Slf4j
import org.springframework.beans.FatalBeanException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.nio.file.Files

@Component
@Slf4j
class FlexGetConfigDAO {

    @Autowired
    FlexGetConfiguration flexGetConfiguration

    FlexGetConfig flexGetConfig

    YAMLFactory yamlFactory = new YAMLFactory()

    void init() {
        log.debug 'Loading {}', flexGetConfiguration.config
        def config = new File(flexGetConfiguration.config)
        if (!config.exists()) {
            log.error 'Missing config: {}', config.absolutePath
            throw new FatalBeanException('Unable to load config file.')
        }
        createBackup(false)
        load()
    }

    def load() {
        ObjectMapper mapper = new ObjectMapper(yamlFactory)
        flexGetConfig = mapper.readValue(new File(flexGetConfiguration.config), FlexGetConfig)
    }

    def write() {
        log.debug 'writing {}', flexGetConfiguration.config
        new File(flexGetConfiguration.config).withOutputStream { fos ->
            yamlFactory.createGenerator(fos).writeObject(flexGetConfig)
        }
    }

    def createBackup(boolean overwrite) {
        def src = new File(flexGetConfiguration.config)
        def dst = new File("${src.canonicalPath}.backup")
        if (!dst.exists() || overwrite) {
            log.info 'Creating backup ...'
            log.info "Overwrite: $overwrite, dst.exists(): ${dst.exists()}"
            Files.copy(src.toPath(), dst.toPath())
        }
    }

}
