package com.recknerd.flexget.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.recknerd.flexget.domain.model.FlexGetConfig
import com.recknerd.flexget.service.SeriesService
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class SeriesControllerSpec extends Specification {

    YAMLFactory yamlFactory = new YAMLFactory()

    def 'getAllSeriesGroups'() {
        setup:
        FlexGetConfig mockFlexGetConfig = new ObjectMapper(yamlFactory).readValue(miniConfig, FlexGetConfig)

        def cut = new SeriesController()
        cut.seriesService = [ getAllSeriesGroups: { return mockFlexGetConfig.templates.template } ] as SeriesService

        when:
        def actual = cut.getAllSeriesGroups()

        then:
        assert actual.hdGroup.size() == 4
        assert actual.stdGroup.size() == 1
    }

    def miniConfig = '''
templates:
  global:
    regexp:
      reject:
      - "ipod"
  tv:
    thetvdb_lookup: true
    verify_ssl_certificates: true
    series:
      hdGroup:
      - "Show 1"
      - Show 2:
          exact: true
      - "Show 3"
      - "Show 4"
      stdGroup:
      - "Show 5"
      settings:
        hdGroup:
          target: "720p hdtv+"
        stdGroup:
          target: "<=720p"
tasks:
  search:
    template: "tv"
'''
}
