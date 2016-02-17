package com.recknerd.flexget.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.recknerd.flexget.domain.model.FlexGetConfig
import com.recknerd.flexget.domain.model.FlexGetSeriesElementSettings
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class SeriesServiceSpec extends Specification {

    static final String RESOURCES = './src/test/resources/'

    YAMLFactory yamlFactory = new YAMLFactory()

    def 'get show from series'() {
        setup:
        def miniConfig = new File(RESOURCES + 'miniConfig.yml')
        FlexGetConfig mockFlexGetConfig = new ObjectMapper(yamlFactory).readValue(miniConfig, FlexGetConfig)

        def cut = new SeriesService()
        cut.yamlManager = [ getFlexGetConfig: { return mockFlexGetConfig } ] as FlexGetConfigDAO

        when:
        def show = cut.getShowFromSeriesGroup('hdGroup', 'Show 2')

        then:
        mockFlexGetConfig.templates.template.series.hdGroup.size() == 4
        mockFlexGetConfig.templates.template.series.stdGroup.size() == 1
        show.title == 'Show 2'
        show.settings.exact == true
    }

    def 'add show to series'() {
        setup:
        def miniConfig = new File(RESOURCES + 'miniConfig.yml')
        FlexGetConfig mockFlexGetConfig = new ObjectMapper(yamlFactory).readValue(miniConfig, FlexGetConfig)

        def cut = new SeriesService()
        cut.yamlManager = [ getFlexGetConfig: { return mockFlexGetConfig }, write: { return } ] as FlexGetConfigDAO

        when:
        def show = cut.addShowToSeriesGroup('stdGroup', 'Show 6', null)

        then:
        show.title == 'Show 6'
        mockFlexGetConfig.templates.template.series.stdGroup.size() == 2
    }

    def 'add show to series with settings'() {
        setup:
        def miniConfig = new File(RESOURCES + 'miniConfig.yml')
        FlexGetConfig mockFlexGetConfig = new ObjectMapper(yamlFactory).readValue(miniConfig, FlexGetConfig)

        def cut = new SeriesService()
        cut.yamlManager = [ getFlexGetConfig: { return mockFlexGetConfig }, write: { return } ] as FlexGetConfigDAO

        when:
        def show = cut.addShowToSeriesGroup('stdGroup', 'Show 6', new FlexGetSeriesElementSettings(exact: true))

        then:
        show.title == 'Show 6'
        show.settings.exact == true
        mockFlexGetConfig.templates.template.series.stdGroup.size() == 2
    }

    def 'remove show from series'() {
        setup:
        def miniConfig = new File(RESOURCES + 'miniConfig.yml')
        FlexGetConfig mockFlexGetConfig = new ObjectMapper(yamlFactory).readValue(miniConfig, FlexGetConfig)

        def cut = new SeriesService()
        cut.yamlManager = [ getFlexGetConfig: { return mockFlexGetConfig }, write: { return } ] as FlexGetConfigDAO

        when:
        cut.removeShowFromSeriesGroup('hdGroup', 'Show 3')

        then:
        mockFlexGetConfig.templates.template.series.hdGroup.size() == 3
    }

}
