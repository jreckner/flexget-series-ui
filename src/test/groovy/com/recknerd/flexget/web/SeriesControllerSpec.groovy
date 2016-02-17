package com.recknerd.flexget.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.recknerd.flexget.domain.model.FlexGetConfig
import com.recknerd.flexget.service.SeriesService
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class SeriesControllerSpec extends Specification {

    static final String RESOURCES = './src/test/resources/'

    YAMLFactory yamlFactory = new YAMLFactory()

    def 'getAllSeriesGroups'() {
        setup:
        def miniConfig = new File(RESOURCES + 'miniConfig.yml')
        FlexGetConfig mockFlexGetConfig = new ObjectMapper(yamlFactory).readValue(miniConfig, FlexGetConfig)

        def cut = new SeriesController()
        cut.seriesService = [ getAllSeriesGroups: { return mockFlexGetConfig.templates.template } ] as SeriesService

        when:
        def actual = cut.getAllSeriesGroups()

        then:
        assert actual.hdGroup.size() == 4
        assert actual.stdGroup.size() == 1
    }

}
