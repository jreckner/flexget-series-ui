package com.recknerd.flexget.service

import com.recknerd.flexget.configuration.NetworkConfiguration
import com.recknerd.flexget.domain.web.TheTvDbShowResponse
import com.recknerd.flexget.domain.web.TvMazeShowResponse
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class TheTvDbShowSearchServiceSpec extends Specification {

    def tvShowName = 'Sherlock'

    def 'get show from tvmaze.com api'() {
        setup:
        def cut = new TheTvDbService()
        cut.networkConfiguration = new NetworkConfiguration(connectionTimeout: 2000, socketTimeout: 5000)

        when:
        TheTvDbShowResponse results = cut.search(tvShowName)
        results.series.each { log.debug("$it")}

        then:
        assert results.series.size() == 8
        assert results.series.find { it.seriesName == 'Sherlock'}
    }

}
