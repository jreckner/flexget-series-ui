package com.recknerd.flexget.service

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.recknerd.flexget.configuration.NetworkConfiguration
import com.recknerd.flexget.domain.web.TheTvDbShowResponse
import com.recknerd.flexget.ex.TheTvDbApiBadRequestException
import com.recknerd.flexget.ex.TheTvDbApiUnavailableException

import groovy.util.logging.Slf4j
import groovy.xml.XmlUtil
import groovyx.net.http.RESTClient
import org.apache.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
@Slf4j
class TheTvDbService {

    @Autowired
    NetworkConfiguration networkConfiguration

    static final String BASE_URI = 'http://thetvdb.com/api/'

    static final String SEARCH_PATH = 'GetSeries.php'

    TheTvDbShowResponse search(String showTitle) {
        log.info 'Searching TheTvDb for {}', showTitle
        def queryParams = ['seriesname': "$showTitle"]
        def resp = thetvdbapi.get(path: SEARCH_PATH, query: queryParams)
        def respAsXml = XmlUtil.serialize(resp.data)
        TheTvDbShowResponse showResponse = new XmlMapper().readValue(respAsXml, TheTvDbShowResponse)
        log.debug 'TheTvDbShowResponse {}', showResponse
        return showResponse
    }

    protected RESTClient getThetvdbapi() {
        def thetvdbApi = new RESTClient("$BASE_URI")
        log.debug 'Create RESTClient Uri: {}', thetvdbApi.uri.toString()

        //HTTPBuilder has no direct methods to add timeouts.  Add them to the HttpParams of the underlying HttpClient
        thetvdbApi.client.params.setParameter('http.connection.timeout', networkConfiguration.connectionTimeout)
        thetvdbApi.client.params.setParameter('http.socket.timeout', networkConfiguration.socketTimeout)

        thetvdbApi.setHeaders([Accept: MediaType.APPLICATION_XML_VALUE])
        if (networkConfiguration.ignoreSSLIssues) { thetvdbApi.ignoreSSLIssues() }
        thetvdbApi.handler.failure = { resp ->
            log.error('TheTVDBAPI Communication Failure: {}', "$resp.status")
            log.error('TheTVDBAPI Response: {}', resp.data)
            if (resp.status == HttpStatus.SC_BAD_REQUEST) {
                log.error 'TheTVDBAPI Bad Request: {}, {}', resp.status, thetvdbApi.uri.toString()
                throw new TheTvDbApiBadRequestException("TheTVDBAPI Bad Request: $resp.status, $resp.data")
            } else if (resp.status < HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                log.error 'TheTVDBAPI Request Failure: {}, {}', resp.status, thetvdbApi.uri.toString()
                throw new TheTvDbApiBadRequestException("TheTVDBAPI Request Failure: $resp.status, $resp.data")
            } else {
                log.error 'TheTVDBAPI Communication Failure: {} at {}', resp.status, thetvdbApi.uri.toString()
                throw new TheTvDbApiUnavailableException("TheTVDBAPI Communication Failure: $resp.status")
            }
        }
        return thetvdbApi
    }
}
