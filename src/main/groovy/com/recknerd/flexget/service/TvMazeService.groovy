package com.recknerd.flexget.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.recknerd.flexget.configuration.NetworkConfiguration
import com.recknerd.flexget.domain.web.TvMazeShowResponse
import com.recknerd.flexget.ex.TvMazeApiBadRequestException
import com.recknerd.flexget.ex.TvMazeApiUnavailableException
import groovy.util.logging.Slf4j
import groovyx.net.http.RESTClient
import net.sf.json.JSONObject
import org.apache.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
@Slf4j
class TvMazeService {

    @Autowired
    NetworkConfiguration networkConfiguration

    static final String BASE_URI = 'http://api.tvmaze.com'

    static final String SEARCH_PATH = '/singlesearch/shows'

    TvMazeShowResponse search(String showTitle) {
        log.info 'Searching TheTvMaze for {}', showTitle
        def queryParams = ['q': "$showTitle"]
        def resp = tvMazeApi.get(path: SEARCH_PATH, query: queryParams)
        def respAsJson = new JSONObject(resp.data).toString()
        TvMazeShowResponse showResponse = new ObjectMapper().readValue(respAsJson, TvMazeShowResponse)
        log.debug 'TheTvDbShowResponse {}', showResponse
        return showResponse
    }

    protected RESTClient getTvMazeApi() {
        def tvmazeApi = new RESTClient("$BASE_URI")
        log.debug 'Create RESTClient Uri: {}', tvmazeApi.uri.toString()

        //HTTPBuilder has no direct methods to add timeouts.  Add them to the HttpParams of the underlying HttpClient
        tvmazeApi.client.params.setParameter('http.connection.timeout', networkConfiguration.connectionTimeout)
        tvmazeApi.client.params.setParameter('http.socket.timeout', networkConfiguration.socketTimeout)

        tvmazeApi.setHeaders([Accept: MediaType.APPLICATION_JSON_VALUE])
        if (networkConfiguration.ignoreSSLIssues) { tvmazeApi.ignoreSSLIssues() }
        tvmazeApi.handler.failure = { resp ->
            log.error('TvMazeApi Communication Failure: {}', "$resp.status")
            log.error('TvMazeApi Response: {}', resp.data)
            if (resp.status == HttpStatus.SC_BAD_REQUEST) {
                log.error 'TvMazeApi Bad Request: {}, {}', resp.status, tvmazeApi.uri.toString()
                throw new TvMazeApiBadRequestException("TvMazeApi Bad Request: $resp.status, $resp.data")
            } else if (resp.status < HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                log.error 'TvMazeApi Request Failure: {}, {}', resp.status, tvmazeApi.uri.toString()
                throw new TvMazeApiBadRequestException("TvMazeApi Request Failure: $resp.status, $resp.data")
            } else {
                log.error 'TvMazeApi Communication Failure: {} at {}', resp.status, tvmazeApi.uri.toString()
                throw new TvMazeApiUnavailableException("TvMazeApi Communication Failure: $resp.status")
            }
        }
        return tvmazeApi
    }
}
