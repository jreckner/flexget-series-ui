package com.recknerd.flexget.service

import com.recknerd.flexget.configuration.NetworkConfiguration
import com.recknerd.flexget.domain.web.TvMazeShowResponse
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class TvMazeShowSearchServiceSpec extends Specification {

    def tvShowName = 'Sherlock'

    def 'get show from tvmaze.com api'() {
        setup:
        def cut = new TvMazeService()
        cut.networkConfiguration = new NetworkConfiguration(connectionTimeout: 2000, socketTimeout: 5000)

        when:
        TvMazeShowResponse results = cut.search(tvShowName)

        then:
        results.name == tvShowName
    }

}
